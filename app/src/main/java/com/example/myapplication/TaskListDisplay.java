package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskListDisplay extends AppCompatActivity implements CustomTaskAdapter.OnTaskLongClickListener {

    private RecyclerView recyclerView;
    private CustomTaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_display);

        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TaskDbHelper dbHelper = new TaskDbHelper(this);
        List<Task> taskList = loadTasksFromDatabase(dbHelper);

        taskAdapter = new CustomTaskAdapter(taskList, this);
        recyclerView.setAdapter(taskAdapter);
        taskAdapter.setOnTaskLongClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        List<Task> taskList = taskAdapter.getTaskList();

        if (taskList != null) {
            outState.putParcelableArrayList("taskList", new ArrayList<>(taskList));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        List<Task> restoredTaskList = savedInstanceState.getParcelableArrayList("taskList");
        if (restoredTaskList != null) {
            taskAdapter.updateTaskList(restoredTaskList);
            taskAdapter.notifyDataSetChanged();
        }
    }

    private List<Task> loadTasksFromDatabase(TaskDbHelper dbHelper) {
        List<Task> tasks = new ArrayList<>();

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String[] projection = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_TITLE,
                TaskContract.TaskEntry.COLUMN_DESCRIPTION,
                TaskContract.TaskEntry.COLUMN_COMPLETED,
                TaskContract.TaskEntry.COLUMN_TIME

        };

        Cursor cursor = null;

        try {
            cursor = database.query(
                    TaskContract.TaskEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    try {
                        String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TITLE));
                        String description = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION));
                        boolean completed = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_COMPLETED)) == 1;
                        long id = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry._ID));
                        String timeString = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TIME));
                        tasks.add(new Task(title, description, completed, id,timeString));
                    } catch (Exception ex) {
                        Log.d("Exception in cursor ", "type" + ex.getMessage());
                    }
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e("SQLiteException", "Ошибка при загрузки данных из бд: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }

        return tasks;
    }


    public void saveCheckBoxState(Task task) {
        Log.d("SaveCheckBox", "Update query: " + task.getTitle());
        Log.d("SaveCheckBox", "Update query: " + task.getId());

        TaskDbHelper dbHelper = new TaskDbHelper(this);
        dbHelper.updateTask(task);
    }

    @Override
    public void onTaskLongClick(Task task) {
        TaskDbHelper dbHelper = new TaskDbHelper(this);
        try {
            dbHelper.deleteTask(task.getId());
            Toast.makeText(this, "Задача удалена", Toast.LENGTH_SHORT).show();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Ошибка при удалении задачи", Toast.LENGTH_SHORT).show();
        }

        List<Task> updatedTaskList = dbHelper.loadTasksFromDatabase();
        taskAdapter.updateTaskList(updatedTaskList);
        taskAdapter.notifyDataSetChanged();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

}
