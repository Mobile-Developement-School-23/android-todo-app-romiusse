package com.romiusse.todoapp.utils

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.romiusse.todoapp.App
import com.romiusse.todoapp.server.transmitter.ServerStatus
import com.romiusse.todoapp.server.transmitter.ServerTransmitter
import com.romiusse.todoapp.todo_list.TodoItem
import com.romiusse.todoapp.todo_list.TodoItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivityWorkManager(var context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    lateinit var serverTransmitter: ServerTransmitter
    lateinit var todoItemsRepository: TodoItemsRepository

    override fun doWork(): Result {

        setServerDataListener()

        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        val token = settings.getString("server_token", "")
        todoItemsRepository = (applicationContext as App).todoItemsRepository
        serverTransmitter = (applicationContext as App).serverTransmitter
        serverTransmitter.TOKEN = token!!

        try{
            CoroutineScope(Dispatchers.IO).launch{
                serverTransmitter.getItems()
            }
        } catch (_: Exception){
            return Result.failure()
        }

        return Result.success()
    }

    private fun setServerDataListener(){
        CoroutineScope(Dispatchers.IO).launch{
                serverTransmitter.listenServerData().collect {

                    if(it.status == ServerStatus.SUCCESS){

                        val list: List<TodoItem> = it.answer!!.map {
                            Utils.convertServerModelToClient(
                                it
                            )
                        }
                        todoItemsRepository.updateAllList(list)
                    }

                }
        }
    }

}