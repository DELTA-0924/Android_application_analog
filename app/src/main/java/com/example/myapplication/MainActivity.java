package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TaskDbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_main);

        Button buttonAddTaskScreen = findViewById(R.id.buttonAddTaskScreen);
        Button buttonTaskListScreen = findViewById(R.id.buttonTaskListScreen);
        NotificationHelper.createNotificationChannel(this);
         dbHelper = new TaskDbHelper(this);

        buttonAddTaskScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Переход на экран добавления задачи
                Intent intent = new Intent(MainActivity.this, AddTaskDisplay.class);
                startActivity(intent);
            }
        });

        buttonTaskListScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Переход на экран списка задач
                Intent intent = new Intent(MainActivity.this, TaskListDisplay.class);
                startActivity(intent);
            }
        });

    }


}
