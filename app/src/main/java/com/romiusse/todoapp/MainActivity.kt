package com.romiusse.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.PeriodicWorkRequest
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
