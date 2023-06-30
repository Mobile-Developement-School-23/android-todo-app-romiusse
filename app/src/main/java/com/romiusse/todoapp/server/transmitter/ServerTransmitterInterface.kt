package com.romiusse.todoapp.server.transmitter

import com.romiusse.todoapp.server.model.ListWrapper
import com.romiusse.todoapp.server.model.ServerTodoItem
import retrofit2.Callback

interface ServerTransmitterInterface {

    var revision: Long

    suspend fun getItems(retryCnt: Int = 0)

    suspend fun mergeItems(jsonList: ListWrapper, retryCnt: Int = 0)


}