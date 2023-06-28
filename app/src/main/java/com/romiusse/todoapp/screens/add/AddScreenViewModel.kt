package com.romiusse.todoapp.screens.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.romiusse.todoapp.todo_list.PriorityItem
import com.romiusse.todoapp.todo_list.TodoItem
import com.romiusse.todoapp.todo_list.TodoItemsRepository
import com.romiusse.todoapp.utils.Utils
import java.util.Date
import kotlin.math.max


class AddScreenViewModel(
    private val todoItemsRepository: TodoItemsRepository
) : ViewModel() {

    private val _item = MutableLiveData<TodoItem>()
    val item: LiveData<TodoItem> = _item


    var isNew = true

    private fun createNewItem() = TodoItem(
        id = Date().time.toString(),
        text = "",
        priority = PriorityItem.BASE,
        flag = false,
        deadline = null,
        createdAt = Date(),
        changedAt = Date()
    )

    fun loadItem(id: String="", new: Boolean=false){
        if(!isNew) return

        if(!new)
            _item.value = todoItemsRepository.getItemFromListById(id)
        else
            _item.value = createNewItem()
        isNew = false
    }

    fun createItem(item: TodoItem?){
        item?.let { todoItemsRepository.addToList(it) }
    }

    fun deleteItem(item: TodoItem?){
        item?.let { todoItemsRepository.removeFromList(it) }
    }

    fun updateItem(item: TodoItem?){
        item?.let { todoItemsRepository.updateFromList(it) }
    }

    fun updateText(text: String) {
        _item.value?.text = text
    }

    fun updateDeadline(deadline: Date?){
        _item.value?.deadline = deadline
    }

}
