package com.romiusse.server_api.transmitter

import com.romiusse.server_api.model.ListWrapper

/**
 *
 * Interface of *ServerTransmitter* class
 *
 * @param apiHelper api helper class
 * @author Romiusse
 */
interface ServerTransmitterInterface {

    var revision: Long

    suspend fun getItems(retryCnt: Int = 0)

    suspend fun mergeItems(jsonList: ListWrapper, retryCnt: Int = 0)


}