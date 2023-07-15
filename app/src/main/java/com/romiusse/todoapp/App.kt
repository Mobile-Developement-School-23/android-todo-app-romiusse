package com.romiusse.todoapp

import android.app.Activity
import android.app.Application
import com.romiusse.edit_todo.di.EditDepsStore
import com.romiusse.todo_list.di.ListDepsStore
import com.romiusse.todoapp.dagger.AppComponent
import com.romiusse.todoapp.dagger.DaggerAppComponent

/**
 * This is the start point of the *app*
 *
 * Creates appComponent and Feature dependencies
 *
 * @author Romiusse
 */
class App: Application() {

    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()

        appComponent =
            DaggerAppComponent.builder()
                .application(this)
                .build()

        ListDepsStore.deps = appComponent
        EditDepsStore.deps = appComponent
    }

    companion object{
        const val DATABASE_NAME = "todo_database.db"
    }

}
