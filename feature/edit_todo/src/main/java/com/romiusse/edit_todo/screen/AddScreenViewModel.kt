package com.romiusse.edit_todo.screen

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.romiusse.todo_repository.PriorityItem
import com.romiusse.todo_repository.TodoItem
import com.romiusse.todo_repository.TodoItemsRepository
import com.romiusse.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Provider


/**
 *
 * View model of add screen
 *
 * @author Romiusse
 */
class AddScreenViewModel @Inject constructor(
    val todoItemsRepository: TodoItemsRepository
) : ViewModel() {


    internal var item: TodoItem = createNewItem()

    var dataSetChangedListener: (String) -> Unit = {}
    var timeSetChangedListener: (String) -> Unit = {}
    var switchSetChangedListener: (Boolean) -> Unit = {}
    var posSetChangedListener: (Int) -> Unit = {}
    var textSetChangedListener: (String) -> Unit = {}

    internal var isNew = true

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

            val itemFromDB = withContext(Dispatchers.IO) {
                return@withContext todoItemsRepository.getItemFromListById(id)
            }
            if (itemFromDB != null) item = itemFromDB

            timeSetChangedListener.invoke(Utils.convertTimeToString(item.notifyTime))
            switchSetChangedListener.invoke(item.deadline != null)
            posSetChangedListener.invoke(Utils.getPosFromPriority(item.priority))
            textSetChangedListener.invoke(item.text)
            dataSetChangedListener.invoke(Utils.convertDateToString(item.deadline))

        }
    }

    fun loadItem(id: String=""){
        if(!isNew) return

        setItemFromListById(id)

        isNew = false
    }

    fun createItem(){
        item.changedAt = Date()
        CoroutineScope(Dispatchers.IO).launch{
            todoItemsRepository.addToList(item)
        }
    }

    fun deleteItem(){
            CoroutineScope(Dispatchers.IO).launch{
                todoItemsRepository.removeFromList(item)
            }
    }

    fun updateItem(){
        item.changedAt = Date()
        CoroutineScope(Dispatchers.IO).launch{
            todoItemsRepository.updateFromList(item)
        }
    }


    fun updateDeadline(deadline: Date?){
        item.deadline = deadline
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
