package com.romiusse.todoapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.romiusse.notifications.R

const val notificationID = "id"
const val channelID = "ToDoApp"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notifications: BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {


        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(com.romiusse.todoapp.R.navigation.nav_graph)
            .setDestination(R.id.addScreenFragment)
            .setArguments(bundleOf("id" to intent.getStringExtra("id")))
            .createPendingIntent()

        val buttonIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(com.romiusse.todoapp.R.navigation.nav_graph)
            .setDestination(R.id.addScreenFragment)
            .setArguments(bundleOf("id" to intent.getStringExtra("id"),
                "delay_day" to "delay_day"))
            .createPendingIntent()

        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setContentIntent(pendingIntent)
            .addAction(0,
                context.getString(com.romiusse.todoapp.R.string.delay_for_day),
                buttonIntent)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        manager.notify(intent.getIntExtra(notificationID, 0), notification)
    }

}