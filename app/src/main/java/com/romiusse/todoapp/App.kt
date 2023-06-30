package com.romiusse.todoapp

import android.app.Application
import com.romiusse.todoapp.server.api.ApiHelper
import com.romiusse.todoapp.server.api.RetrofitBuilder
import com.romiusse.todoapp.server.transmitter.ServerTransmitter
import com.romiusse.todoapp.todo_list.room.AppDatabase
import com.romiusse.todoapp.todo_list.TodoItemsRepository

class App: Application() {

    lateinit var serverTransmitter: ServerTransmitter
    lateinit var dataBase: AppDatabase
    lateinit var todoItemsRepository: TodoItemsRepository


    override fun onCreate() {
        super.onCreate()
        serverTransmitter = ServerTransmitter(ApiHelper(RetrofitBuilder.apiService))
        dataBase = AppDatabase.buildDatabase(applicationContext, DATABASE_NAME)
        todoItemsRepository = TodoItemsRepository(dataBase.todoItemDao())

    }

    companion object{
        const val DATABASE_NAME = "todo_database.db"
    }

}