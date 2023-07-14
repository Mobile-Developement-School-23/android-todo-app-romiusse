package com.romiusse.notifications

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.Navigation

const val notificationID = "id"
const val channelID = "ToDoApp"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notifications: BroadcastReceiver() {


    @SuppressLint("ResourceType")
    override fun onReceive(context: Context, intent: Intent) {

        //val pendingIntent = NavDeepLinkBuilder(context)
         //   .setGraph()
         //   .setDestination(R.id.destination)
         //   .setArguments(bundle)
        //    .createPendingIntent()


        //val contentIntent = Intent(context, MainActivity::class.java)
        //contentIntent.putExtra("id", )

        //val pIntent =
        //    getActivity(context, 0, contentIntent,
        //        PendingIntent.FLAG_MUTABLE)

        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
         //   .setContentIntent(pIntent)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        manager.notify(intent.getIntExtra(notificationID, 0), notification)
    }

}