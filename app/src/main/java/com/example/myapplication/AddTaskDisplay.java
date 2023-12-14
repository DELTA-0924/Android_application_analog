package com.example.myapplication;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTaskDisplay extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button buttonAddTask;

    private TaskDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_add_task);

        dbHelper = new TaskDbHelper(this);

        String dbPath = dbHelper.getDatabasePath();
        Log.d("DatabasePath", "Path: " + dbPath);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonAddTask = findViewById(R.id.buttonAddTask);

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Получаем введенные значения
                String title = editTextTitle.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();

                // Проверяем, что поля не пустые
                if (title.isEmpty() || description.isEmpty()) {
                    // Выводим сообщение об ошибке, если поля пустые
                    Toast.makeText(getApplicationContext(), "Поля не могут быть пустыми", Toast.LENGTH_SHORT).show();
                } else {
                    // Сохраняем задачу в базе данных
                    saveTask(title, description);

                    // Закрываем активность
                    finish();
                }
            }
        });
    }

    private void saveTask(String title, String description) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TITLE, title);
        values.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, description);

        try {
            long newRowId = database.insertOrThrow(TaskContract.TaskEntry.TABLE_NAME, null, values);
            if (newRowId != -1) {

                Toast.makeText(getApplicationContext(), "Задача сохранена успешно", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(getApplicationContext(), "Не удалось сохранить задачу", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Ошибка при сохранении задачи", Toast.LENGTH_SHORT).show();
        } finally {
            database.close();
        }
    }


}
