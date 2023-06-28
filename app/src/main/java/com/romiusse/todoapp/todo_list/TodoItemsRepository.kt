package com.romiusse.todoapp.todo_list

import android.R.attr.value
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.color
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale


typealias ToDoItemListener = (data: List<TodoItem>) -> Unit

class TodoItemsRepository(private val todoItemDao: TodoItemDao): TodoItemsRepositoryInterface{

    override suspend fun addToList(toDoItem: TodoItem){
        todoItemDao.addToList(toDoItem)
    }

    override suspend fun updateFromList(toDoItem: TodoItem){
        todoItemDao.updateFromList(toDoItem)
    }

    override suspend fun getItemFromListById(id: String): TodoItem? {
        return todoItemDao.getItemFromListById(id)
    }

    override suspend fun removeFromList(toDoItem: TodoItem){
        todoItemDao.removeFromList(toDoItem)
    }

    override fun getList(): Flow<List<TodoItem>>{
        return todoItemDao.getList()
    }

}