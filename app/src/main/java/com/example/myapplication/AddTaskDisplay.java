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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTaskDisplay extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextTime;
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
        editTextTime = findViewById(R.id.editTextTime);
        buttonAddTask = findViewById(R.id.buttonAddTask);
        buttonAddTask = findViewById(R.id.buttonAddTask);

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Получаем введенные значения
                String title = editTextTitle.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String time = editTextTime.getText().toString().trim();

                // Проверяем, что поля не пустые
                if (title.isEmpty() || description.isEmpty()) {
                    // Выводим сообщение об ошибке, если поля пустые
                    Toast.makeText(getApplicationContext(), "Поля не могут быть пустыми", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Проверяем формат времени и его валидность
                if (isValidTime(time)) {
                    Toast.makeText(getApplicationContext(), "Введите корректное время в формате hh:mm:ss, позже текущего времени", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Сохраняем задачу в базе данных
                saveTask(title, description, time);
                AlarmScheduler.scheduleAlarm(getApplicationContext(), time, "Напоминание", "Задача: " + title);

                // Закрываем активность
                finish();
            }
        });
    }

    private boolean isValidTime(String time) {
        // Проверяем формат hh:mm:ss
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        sdf.setLenient(false);
        try {
            Date inputTime = sdf.parse(time); // Парсим введенное время
            Calendar inputCalendar = Calendar.getInstance();
            inputCalendar.setTime(inputTime);

            // Получаем текущее время
            Calendar now = Calendar.getInstance();

            // Устанавливаем дату на текущий день для корректного сравнения
            Calendar currentTime = Calendar.getInstance();
            currentTime.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
            currentTime.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
            currentTime.set(Calendar.SECOND, now.get(Calendar.SECOND));

            // Сравниваем время
            return inputCalendar.getTime().after(currentTime.getTime());
        } catch (ParseException e) {
            // Исключение, если формат некорректный
            return false;
        }
    }
    private void saveTask(String title, String description,String time) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TITLE, title);
        values.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, description);
        values.put(TaskContract.TaskEntry.COLUMN_TIME, time);


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
