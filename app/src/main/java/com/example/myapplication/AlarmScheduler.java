package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmScheduler {

    public static void scheduleAlarm(Context context, String time, String title, String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        try {
            Calendar calendar = Calendar.getInstance();
            Calendar alarmTime = Calendar.getInstance();
            alarmTime.setTime(sdf.parse(time));

            // Устанавливаем время для сигнала
            alarmTime.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            alarmTime.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            alarmTime.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

            // Проверяем, чтобы время не было в прошлом
            if (alarmTime.before(calendar)) {
                alarmTime.add(Calendar.DAY_OF_YEAR, 1); // Устанавливаем на следующий день
            }

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("title", title);
            intent.putExtra("message", message);

            PendingIntent alarmIntent = PendingIntent.getBroadcast(
                    context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            // Устанавливаем будильник
            if (alarmManager != null) {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), alarmIntent);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}