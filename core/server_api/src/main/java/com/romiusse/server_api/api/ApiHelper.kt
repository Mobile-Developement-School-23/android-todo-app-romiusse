package com.romiusse.server_api.api

import com.romiusse.server_api.model.ListWrapper

class ApiHelper(private val apiService: ApiService) {

    suspend fun getItems(token: String) = apiService.getItems(token)

    suspend fun mergeItems(token: String, revision: Long, jsonList: ListWrapper) =
        apiService.mergeItems(token, revision, jsonList)
}
