package com.romiusse.todoapp

import android.app.Application
import com.romiusse.todoapp.server.api.ApiHelper
import com.romiusse.todoapp.server.api.RetrofitBuilder
import com.romiusse.todoapp.server.transmitter.ServerTransmitter
import com.romiusse.todoapp.todo_list.TodoItemsRepository

class App: Application() {

    val todoItemsRepository = TodoItemsRepository()
    val serverTransmitter = ServerTransmitter(ApiHelper(RetrofitBuilder.apiService))

}