package com.romiusse.todoapp.server.transmitter

import com.romiusse.todoapp.server.api.ApiHelper
import com.romiusse.todoapp.server.model.ListWrapper
import com.romiusse.todoapp.server.model.ServerTodoItem
import com.romiusse.todoapp.todo_list.TodoItem

//TODO Remove this :)
//"dithering": "Matveev_R"

class ServerTransmitter(private val apiHelper: ApiHelper) : ServerTransmitterInterface {

    private val token = "Bearer dithering"

    override var revision: Long = 0

    override suspend fun getItems(): List<ServerTodoItem>{
        val listWrapper: ListWrapper = apiHelper.getItems(token)
        return listWrapper.list
    }

    override suspend fun mergeItems(token: String, revision: String, jsonList: String):
            List<ServerTodoItem>{
        val listWrapper = apiHelper.mergeItems(token, revision, jsonList) as ListWrapper
        return listWrapper.list
    }

}