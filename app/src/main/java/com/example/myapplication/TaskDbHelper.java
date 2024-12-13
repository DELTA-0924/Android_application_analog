package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class TaskDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TASKS_TABLE = "CREATE TABLE " +
                TaskContract.TaskEntry.TABLE_NAME + " (" +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_DESCRIPTION + " TEXT," +
                TaskContract.TaskEntry.COLUMN_COMPLETED + " INTEGER DEFAULT 0," +
                TaskContract.TaskEntry.COLUMN_TIME + " TIME)";

        db.execSQL(SQL_CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }
    public String getDatabasePath() {
        // Получаем путь к базе данных
        SQLiteDatabase db = getWritableDatabase();
        String path = db.getPath();
        db.close();
        return path;
    }
    public void deleteTask(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE_NAME, TaskContract.TaskEntry._ID + "=?",
                new String[]{String.valueOf(taskId)});
        db.close();
    }

    public List<Task> loadTasksFromDatabase() {
        List<Task> tasks = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_TITLE,
                TaskContract.TaskEntry.COLUMN_DESCRIPTION,
                TaskContract.TaskEntry.COLUMN_COMPLETED,
                TaskContract.TaskEntry.COLUMN_TIME
        };
        return tasks;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TaskContract.TaskEntry.COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);

        int updatedRows = db.update(
                TaskContract.TaskEntry.TABLE_NAME,
                values,
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(task.getId())});

        db.close();

        Log.d("Database", "Updated rows: " + updatedRows); // Вывод количества обновленных строк
        String updateQuery = "UPDATE " + TaskContract.TaskEntry.TABLE_NAME +
                " SET " + TaskContract.TaskEntry.COLUMN_COMPLETED + " = " + (task.isCompleted() ? 1 : 0) +
                " WHERE " + TaskContract.TaskEntry._ID + " = " + task.getId();
        Log.d("Database", "Update query: " + updateQuery);

        return updatedRows; // Возвращаем количество обновленных строк
    }
}
