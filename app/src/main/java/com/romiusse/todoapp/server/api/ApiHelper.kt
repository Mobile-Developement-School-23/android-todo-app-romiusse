package com.romiusse.todoapp.server.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getItems(token: String) = apiService.getItems(token)

    suspend fun mergeItems(token: String, revision: String, jsonList: String) =
        apiService.mergeItems(token, revision, jsonList)
}