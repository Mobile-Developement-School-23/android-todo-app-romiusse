package com.romiusse.todoapp.server.transmitter

import com.romiusse.todoapp.server.api.ApiHelper
import com.romiusse.todoapp.server.model.ListWrapper
import com.romiusse.todoapp.server.model.ServerTodoItem
import com.romiusse.todoapp.todo_list.TodoItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import java.net.SocketTimeoutException


private const val MAX_RETRY = 3

typealias ServerDataListener = (data: ServerAnswer<List<ServerTodoItem>>) -> Unit

class ServerTransmitter(private val apiHelper: ApiHelper) : ServerTransmitterInterface {

    private val serverDataListeners = mutableSetOf<ServerDataListener>()
    override var revision: Long = 0
    var TOKEN = "Bearer dithering"

    override suspend fun getItems(retryCnt: Int) {

        notifyListeners(createAnswer(ServerStatus.LOADING, requestName = "getItems"))

        try {
            val listWrapper: ListWrapper = apiHelper.getItems(TOKEN)
            val list = listWrapper.list
            listWrapper.revision?.let { revision = it }
            notifyListeners(createAnswer(ServerStatus.SUCCESS, list,"getItems"))

        } catch (e: Exception){
            exceptionsParser(e, "getItems", retryCnt)
        }
    }

    override suspend fun mergeItems(jsonList: ListWrapper, retryCnt: Int) {

        notifyListeners(createAnswer(ServerStatus.LOADING, requestName = "mergeItems"))

        try {
            val listWrapper = apiHelper.mergeItems(TOKEN, revision, jsonList) as ListWrapper
            val list = listWrapper.list
            listWrapper.revision?.let { revision = it }
            notifyListeners(createAnswer(ServerStatus.SUCCESS, list,"mergeItems"))

        } catch (e: Exception){
            exceptionsParser(e, "mergeItems", retryCnt, jsonList)
        }
    }

    private fun createAnswer(status: ServerStatus, answer: List<ServerTodoItem>?=null,
                             requestName: String?, error: ServerErrors? = null,
                             info: String? = null) = ServerAnswer(
        status = status,
        answer = answer,
        requestName = requestName,
        error = error,
        info = info
    )


    private suspend fun exceptionsParser(exception: Exception, requestName: String, retryCnt: Int,
                                         vararg arg: ListWrapper){

        val error = when (exception) {
            is SocketTimeoutException -> {

                ServerErrors.SOCKET_TIME_OUT

            }
            is retrofit2.HttpException -> {
                val code = when(exception.code()){
                    400 -> ServerErrors.WRONG_REQUEST
                    401 -> ServerErrors.WRONG_AUTH
                    404 -> ServerErrors.ELEMENT_NOT_FOUND
                    500 -> ServerErrors.SERVER_ERROR
                    else -> ServerErrors.SERVER_ERROR
                }
                code
            }
            else -> {
                ServerErrors.SERVER_ERROR
            }
        }

        if(retryCnt >= MAX_RETRY)
            notifyListeners(createAnswer(ServerStatus.ERROR, requestName = requestName,
                error = error))
        else{

            notifyListeners(createAnswer(ServerStatus.RETRYING, requestName = requestName,
                info = "${retryCnt + 1} / $MAX_RETRY"))

            delay(500)

            when(requestName){

                "getItems" -> getItems(retryCnt + 1)
                "mergeItems" -> mergeItems(arg[0], retryCnt + 1)

                else -> {}

            }

        }

    }

    private fun notifyListeners(answer: ServerAnswer<List<ServerTodoItem>>){
        serverDataListeners.forEach { it.invoke(answer) }
    }

    fun listenServerData(): Flow<ServerAnswer<List<ServerTodoItem>>> = callbackFlow {

        val listener: ServerDataListener = { trySend(it) }
        serverDataListeners.add(listener)
        awaitClose { serverDataListeners.remove(listener) }

    }.buffer(Channel.CONFLATED)

}