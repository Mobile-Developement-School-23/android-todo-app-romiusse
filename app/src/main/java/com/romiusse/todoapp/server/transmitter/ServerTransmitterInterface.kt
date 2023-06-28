package com.romiusse.todoapp.server.transmitter

import com.romiusse.todoapp.server.model.ListWrapper
import com.romiusse.todoapp.server.model.ServerTodoItem
import retrofit2.Callback

interface ServerTransmitterInterface {

    var revision: Long

    suspend fun getItems(): List<ServerTodoItem>

    suspend fun mergeItems(token: String, revision: String, jsonList: String) :
            List<ServerTodoItem>


}