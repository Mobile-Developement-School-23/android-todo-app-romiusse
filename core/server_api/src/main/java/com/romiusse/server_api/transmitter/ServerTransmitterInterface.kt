package com.romiusse.server_api.transmitter

import com.romiusse.server_api.model.ListWrapper

interface ServerTransmitterInterface {

    var revision: Long

    suspend fun getItems(retryCnt: Int = 0)

    suspend fun mergeItems(jsonList: ListWrapper, retryCnt: Int = 0)


}