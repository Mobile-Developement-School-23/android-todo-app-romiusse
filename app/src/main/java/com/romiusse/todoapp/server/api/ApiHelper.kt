package com.romiusse.todoapp.server.api

import com.romiusse.todoapp.server.model.ListWrapper

class ApiHelper(private val apiService: ApiService) {

    suspend fun getItems(token: String) = apiService.getItems(token)

    suspend fun mergeItems(token: String, revision: Long, jsonList: ListWrapper) =
        apiService.mergeItems(token, revision, jsonList)
}
