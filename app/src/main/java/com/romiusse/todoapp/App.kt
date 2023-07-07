package com.romiusse.todoapp

import android.app.Application
import com.romiusse.edit_todo.di.EditDeps
import com.romiusse.edit_todo.di.EditDepsStore
import com.romiusse.todo_list.di.ListDepsStore
import com.romiusse.todoapp.dagger.AppBottomUtilsModule
import com.romiusse.todoapp.dagger.AppComponent
import com.romiusse.todoapp.dagger.AppDataBaseModule
import com.romiusse.todoapp.dagger.AppItemsRepositoryModule
import com.romiusse.todoapp.dagger.AppServerModule
import com.romiusse.todoapp.dagger.DaggerAppComponent

/**
 * This is the start point of the *app*
 *
 * Creates appComponent and Feature dependencies
 *
 * @author Romiusse
 */
class App: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }


    override fun onCreate() {
        super.onCreate()
        ListDepsStore.deps = appComponent
        EditDepsStore.deps = appComponent
    }


    companion object{
        const val DATABASE_NAME = "todo_database.db"
    }

}
