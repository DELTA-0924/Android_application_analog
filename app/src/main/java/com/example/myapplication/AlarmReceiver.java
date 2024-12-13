package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Получаем данные из интента
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        System.out.println("Setnb notification");
        // Отправляем уведомление
        NotificationHelper.sendNotification(context, title, message);
    }
}