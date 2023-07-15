package com.romiusse.notifications

import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import java.util.Date

class NotificationActions(val context: Context, val notifyClass: Class<out Any>) {

    fun removeScheduleNotification(code: Int){
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, notifyClass)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            code,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }
    fun scheduleNotification(code: Int, notifyTime: Date, id: String)
    {
        val intent = Intent(context, notifyClass)
        intent.putExtra("titleExtra", "Осталось мало времени")
        intent.putExtra("messageExtra", "Поспешите, уже сегодня закончится срок выполнения вашей задачи")
        intent.putExtra("id", id)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            code,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = notifyTime.time
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    fun createNotificationChannel()
    {
        val name = "TodoApp"
        val desc = "Уведомления о дедлайнах"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("ToDoApp", name, importance)
        channel.description = desc
        val notificationManager =
            context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE)
                    as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}