package com.romiusse.todoapp.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.romiusse.todoapp.server.model.ListWrapper
import com.romiusse.todoapp.server.model.ServerTodoItem
import com.romiusse.todoapp.server.transmitter.ServerAnswer
import com.romiusse.todoapp.server.transmitter.ServerStatus
import com.romiusse.todoapp.server.transmitter.ServerTransmitter
import com.romiusse.todoapp.todo_list.TodoItem
import com.romiusse.todoapp.todo_list.TodoItemsRepository
import com.romiusse.todoapp.utils.Utils
import com.romiusse.todoapp.utils.Utils.convertClientModelToServer
import com.romiusse.todoapp.utils.Utils.convertServerModelToClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.Date

class MainScreenViewModel(
    private val todoItemsRepository: TodoItemsRepository,
    private val serverTransmitter: ServerTransmitter
    ) : ViewModel() {

    private val _items = MutableLiveData<List<TodoItem>>()
    val items:LiveData<List<TodoItem>> = _items

    private val _info = MutableLiveData<String>()
    val info:LiveData<String> = _info


    private fun setItemsListener(){
        viewModelScope.launch {
            todoItemsRepository.
            getList().collect{ list ->
                _items.value = list
                viewModelScope.launch(Dispatchers.IO){
                    serverTransmitter.mergeItems(
                            ListWrapper(list = list.map { convertClientModelToServer(it) })
                    )
                }

            }
        }
    }

    private fun setServerDataListener(){
        viewModelScope.launch{
            serverTransmitter.listenServerData().collect{
                parseServerData(it)
            }
        }
    }


    private fun parseServerData(answer: ServerAnswer<List<ServerTodoItem>>){

        when(answer.status){

            ServerStatus.SUCCESS ->{
                _info.value = "Данный обновлены"
                val answer: List<ServerTodoItem>? = answer.answer
                answer?.let {
                    _items.value = answer.map { convertServerModelToClient(it) }
                }

            }
            ServerStatus.RETRYING->{
                _info.value = "Ошибка сервера. Отправляю зарос еще раз ${answer.info}"
            }
            ServerStatus.ERROR->{
                _info.value = "Ошибка. ${answer.error.toString()}"
            }

            else -> {}
        }


    }

    init{
        setItemsListener()
        setServerDataListener()

        //loadData()

    }
    private fun loadData(){

        viewModelScope.launch(Dispatchers.IO){
            serverTransmitter.getItems()
        }

    }

    fun updateFromList(todoItem: TodoItem){
        todoItem.changedAt = Date()
        viewModelScope.launch(Dispatchers.IO) {
            todoItemsRepository.updateFromList(todoItem)
        }
    }


}