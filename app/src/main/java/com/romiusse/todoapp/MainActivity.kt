package com.romiusse.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkRequest
import androidx.work.impl.WorkRequestHolder
import com.romiusse.todoapp.utils.MainActivityWorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //OneTimeWorkRequest.Builder(MainActivityWorkManager::class.java).build()

        PeriodicWorkRequest.Builder(MainActivityWorkManager::class.java,8, TimeUnit.HOURS)
            .build()

    }
}