package com.romiusse.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.PeriodicWorkRequest
import com.romiusse.edit_todo.di.EditDepsStore
import com.romiusse.todo_list.di.ListDepsStore
import com.romiusse.todoapp.dagger.AppComponent
import com.romiusse.todoapp.dagger.DaggerAppComponent
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


    }


}
