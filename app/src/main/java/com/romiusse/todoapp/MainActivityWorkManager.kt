package com.romiusse.todoapp

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.romiusse.server_api.transmitter.ServerTransmitter
import com.romiusse.todo_repository.TodoItem
import com.romiusse.todo_repository.TodoItemsRepository
import com.romiusse.todoapp.server.transmitter.ServerStatus
import com.romiusse.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivityWorkManager(var context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    @Inject
    lateinit var serverTransmitter: ServerTransmitter

    @Inject
    lateinit var todoItemsRepository: TodoItemsRepository

    override fun doWork(): Result {

        setServerDataListener()

        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        val token = settings.getString("server_token", "")
        (applicationContext as App).appComponent.inject(this)
        serverTransmitter.token = token!!

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