package com.romiusse.server_api.api

import com.romiusse.server_api.model.ListWrapper
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH

/**
 * Api interface
 *
 * Contains 2 functions get and set
 *
 * @author Romiusse
 */
interface ApiService {


    @GET("todobackend/list")
    suspend fun getItems(@Header("Authorization") token: String): ListWrapper

    @Headers("Content-Type: application/json")
    @PATCH("todobackend/list")
    suspend fun mergeItems(@Header("Authorization") token: String,
                           @Header("X-Last-Known-Revision") revision: Long,
                           @Body jsonList: ListWrapper
    ): ListWrapper

}
