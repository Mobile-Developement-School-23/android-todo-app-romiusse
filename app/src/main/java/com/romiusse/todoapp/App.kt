package com.romiusse.todoapp

import android.app.Application
import com.romiusse.todoapp.dagger.AppBottomUtilsModule
import com.romiusse.todoapp.dagger.AppComponent
import com.romiusse.todoapp.dagger.AppDataBaseModule
import com.romiusse.todoapp.dagger.AppItemsRepositoryModule
import com.romiusse.todoapp.dagger.AppServerModule
import com.romiusse.todoapp.dagger.DaggerAppComponent
import com.romiusse.todoapp.screens.main.BottomSheetUtils
import com.romiusse.todoapp.server.api.ApiHelper
import com.romiusse.todoapp.server.api.RetrofitBuilder
import com.romiusse.todoapp.server.transmitter.ServerTransmitter

class App: Application() {

    lateinit var appComponent: AppComponent


    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appServerModule(AppServerModule())
            .appDataBaseModule(AppDataBaseModule(this))
            .appBottomUtilsModule(AppBottomUtilsModule())
            .appItemsRepositoryModule(AppItemsRepositoryModule())
            .build()
        //appComponent.inject(this)

    }

    companion object{
        const val DATABASE_NAME = "todo_database.db"
    }

}
