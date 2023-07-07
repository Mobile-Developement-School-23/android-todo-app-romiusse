package com.romiusse.server_api.api

import com.romiusse.server_api.model.ListWrapper

/**
 * Api helper class
 *
 * Contains 2 functions get and set
 *
 * @author Romiusse
 */
class ApiHelper(private val apiService: ApiService) {

    suspend fun getItems(token: String) = apiService.getItems(token)

    suspend fun mergeItems(token: String, revision: Long, jsonList: ListWrapper) =
        apiService.mergeItems(token, revision, jsonList)
}
