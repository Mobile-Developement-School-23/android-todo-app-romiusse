package com.romiusse.edit_todo.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.romiusse.todo_repository.PriorityItem
import com.romiusse.todo_repository.TodoItem
import com.romiusse.todo_repository.TodoItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Provider


internal class AddScreenViewModel @Inject constructor(
    val todoItemsRepository: TodoItemsRepository
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

    private fun setItemFromListById(id: String){
        viewModelScope.launch {

            val item = withContext(Dispatchers.IO) {
                return@withContext todoItemsRepository.getItemFromListById(id)
            }
            if (item != null) _item.value = item!!
            else _item.value = createNewItem()
        }
    }

    fun loadItem(id: String="", new: Boolean=false){
        if(!isNew) return

        if(!new)
            setItemFromListById(id)
        else {
            _item.value = createNewItem()
        }
    }

    fun createItem(item: TodoItem?){
        item?.let {
            CoroutineScope(Dispatchers.IO).launch{
                todoItemsRepository.addToList(it)
            }
        }
    }

    fun deleteItem(item: TodoItem?){
        item?.let {
            CoroutineScope(Dispatchers.IO).launch{
                todoItemsRepository.removeFromList(it)
            }}
    }

    fun updateItem(item: TodoItem?){
        item?.let {
            CoroutineScope(Dispatchers.IO).launch{
                todoItemsRepository.updateFromList(it)
            }}
    }

    fun updateText(text: String) {
        _item.value?.text = text
    }

    fun updateDeadline(deadline: Date?){
        _item.value?.deadline = deadline
    }

    class Factory @Inject constructor(
        private val todoItemsRepository: Provider<TodoItemsRepository>
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == AddScreenViewModel::class.java)
            return AddScreenViewModel(todoItemsRepository.get()) as T
        }
    }

}
