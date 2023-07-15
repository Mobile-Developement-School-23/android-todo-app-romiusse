package com.romiusse.todoapp

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.PeriodicWorkRequest
import com.google.mlkit.common.sdkinternal.CommonUtils
import com.romiusse.edit_todo.di.EditDepsStore
import com.romiusse.settings.DARK
import com.romiusse.settings.LIGHT
import com.romiusse.settings.SYSTEM
import com.romiusse.settings.selectedTheme
import com.romiusse.settings.themeStyle
import com.romiusse.todo_list.di.ListDepsStore
import com.romiusse.todoapp.dagger.AppComponent
import com.romiusse.todoapp.dagger.DaggerAppComponent
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

/**
 * Main activity class
 *
 * This class do nothing
 *
 * @author Romiusse
 */
class MainActivity : AppCompatActivity() {

    companion object{
        var mActivitym: WeakReference<Activity>? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        mActivitym = WeakReference(this)

        val sharedPreference =
            applicationContext.getSharedPreferences(themeStyle, Context.MODE_PRIVATE)
        val type = sharedPreference.getInt(selectedTheme, SYSTEM)

        if(type == DARK){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else if(type == LIGHT) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }


}
