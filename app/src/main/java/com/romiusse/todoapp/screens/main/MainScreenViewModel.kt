package com.romiusse.todoapp.screens.main

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romiusse.todoapp.server.model.ListWrapper
import com.romiusse.todoapp.server.model.ServerTodoItem
import com.romiusse.todoapp.server.transmitter.ServerAnswer
import com.romiusse.todoapp.server.transmitter.ServerErrors
import com.romiusse.todoapp.server.transmitter.ServerStatus
import com.romiusse.todoapp.server.transmitter.ServerTransmitter
import com.romiusse.todoapp.todo_list.TodoItem
import com.romiusse.todoapp.todo_list.TodoItemsRepository
import com.romiusse.todoapp.utils.Utils.convertClientModelToServer
import com.romiusse.todoapp.utils.Utils.convertServerModelToClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date


const val DATA_WAS_UPDATED = "DATA_WAS_UPDATED"
const val CONNECTION_TIME_OUT = "CONNECTION_TIME_OUT"
const val WRONG_AUTH = "WRONG_AUTH"
const val SERVER_ERROR = "SERVER_ERROR"

class MainScreenViewModel(
    private val todoItemsRepository: TodoItemsRepository,
    private val serverTransmitter: ServerTransmitter
    ) : ViewModel() {

    private val _items = MutableLiveData<List<TodoItem>>()
    val items: LiveData<List<TodoItem>> = _items

    private val _info = MutableLiveData<String>()
    val info: LiveData<String> = _info

    private val _isInternetConnected = MutableLiveData<Boolean>(false)
    val isInternetConnected: LiveData<Boolean> = _isInternetConnected

    private val _syncIconStatus = MutableLiveData<SyncIconStatus>()
    val syncIconStatus: LiveData<SyncIconStatus> = _syncIconStatus

    private var isSynchronized = false

    private fun setItemsListener(){
        viewModelScope.launch {
            todoItemsRepository.
            getList().collect{ list ->
                _items.value = list

                if(isSynchronized && _isInternetConnected.value != null &&
                    _isInternetConnected.value!!) mergeItems(list)
            }
        }
    }

    private fun mergeItems(list: List<TodoItem>){
        viewModelScope.launch(Dispatchers.IO){
            serverTransmitter.mergeItems(
                ListWrapper(list = list.map { convertClientModelToServer(it) })
            )
        }
    }

    private fun setServerDataListener(){
        viewModelScope.launch{
            serverTransmitter.listenServerData().collect{
                parseServerData(it)
            }
        }
    }

    private fun updateAllList(list: List<TodoItem>){
        viewModelScope.launch(Dispatchers.IO) {
            todoItemsRepository.updateAllList(list)
        }
    }

    val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _isInternetConnected.postValue(true)

            if(!isSynchronized) loadData()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _isInternetConnected.postValue(false)
            _syncIconStatus.postValue(SyncIconStatus.ERROR)
        }
    }

    private fun parseServerData(answer: ServerAnswer<List<ServerTodoItem>>){


        when(answer.status){

            ServerStatus.LOADING->{
                _syncIconStatus.value = SyncIconStatus.SYNCHRONIZING
            }
            ServerStatus.SUCCESS ->{

                val list: List<TodoItem> = answer.answer!!.map { convertServerModelToClient(it) }

                if(!isSynchronized){
                    updateAllList(list)
                    isSynchronized = true
                    _info.value = DATA_WAS_UPDATED
                }

                _items.value = list

                _syncIconStatus.value = SyncIconStatus.OK

            }
            ServerStatus.RETRYING->{
                _info.value = "Ошибка сервера. Отправляю запрос еще раз ${answer.info}"
                _syncIconStatus.value = SyncIconStatus.SYNCHRONIZING
            }
            ServerStatus.ERROR->{

                _info.value = when(answer.error){
                    ServerErrors.SOCKET_TIME_OUT -> CONNECTION_TIME_OUT
                    ServerErrors.WRONG_AUTH -> WRONG_AUTH
                    else -> SERVER_ERROR
                }

                _syncIconStatus.value = SyncIconStatus.ERROR

            }
        }


    }

    init{
        setItemsListener()
        setServerDataListener()

    }
    private fun loadData(){

        viewModelScope.launch(Dispatchers.IO){
            serverTransmitter.getItems()
            //if(_items.value != null)
            //    serverTransmitter.mergeItems(
            //        ListWrapper(list = _items.value!!.map { convertClientModelToServer(it) }))
        }

    }

    fun updateFromList(todoItem: TodoItem){
        todoItem.changedAt = Date()
        viewModelScope.launch(Dispatchers.IO) {
            todoItemsRepository.updateFromList(todoItem)
        }
    }


}