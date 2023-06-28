package com.romiusse.todoapp.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romiusse.todoapp.server.transmitter.ServerTransmitter
import com.romiusse.todoapp.todo_list.ToDoItemListener
import com.romiusse.todoapp.todo_list.TodoItem
import com.romiusse.todoapp.todo_list.TodoItemsRepository
import com.romiusse.todoapp.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainScreenViewModel(
    private val todoItemsRepository: TodoItemsRepository,
    private val serverTransmitter: ServerTransmitter
    ) : ViewModel() {

    private val _items = MutableLiveData<List<TodoItem>>()
    val items:LiveData<List<TodoItem>> = _items

    //private val toDoItemListener: ToDoItemListener = { _items.value = it }

    init{
        loadDate()

        //viewModelScope.launch{
        //    val dateFromServer = getServerDate()
        //    dateFromServer?.let { todoItemsRepository.setList(it) }
        //}
    }
    override fun onCleared() {
        super.onCleared()
        //todoItemsRepository.removeListener(toDoItemListener)

    }
    private fun loadDate(){

        viewModelScope.launch {
            todoItemsRepository.
            getList().collect{
                _items.value = it
            }
        }

        //todoItemsRepository.addListener(toDoItemListener)
    }

    fun updateFromList(todoItem: TodoItem){
        viewModelScope.launch(Dispatchers.IO) {
            todoItemsRepository.updateFromList(todoItem)
        }
    }

    private suspend fun getServerDate() = withContext(Dispatchers.IO){
        try {
            return@withContext serverTransmitter.getItems()
                .map { Utils.convertServerModelToClient(it) }

        } catch (exception: Exception) {
            //TODO(Add catch exceptions)
            return@withContext null
        }
    }

}