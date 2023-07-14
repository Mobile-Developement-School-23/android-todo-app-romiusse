package com.romiusse.todoapp

import android.Manifest
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.PeriodicWorkRequest
import com.romiusse.notifications.Notifications
import com.romiusse.notifications.channelID
import com.romiusse.notifications.isNotifyPermGranted
import com.romiusse.notifications.messageExtra
import com.romiusse.notifications.permissionPreferences
import com.romiusse.notifications.titleExtra
import com.romiusse.todoapp.databinding.ActivityMainBinding
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * Main activity class
 *
 * This class do nothing
 *
 * @author Romiusse
 */
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        PeriodicWorkRequest.Builder(
            MainActivityWorkManager::class.java,
            8,
            TimeUnit.HOURS)
            .build()


    }


}
