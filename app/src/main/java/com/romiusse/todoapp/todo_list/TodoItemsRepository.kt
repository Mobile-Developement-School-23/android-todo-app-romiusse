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
import java.text.SimpleDateFormat
import java.util.Locale


typealias ToDoItemListener = (data: List<TodoItem>) -> Unit

class TodoItemsRepository(){

    private val listeners = mutableListOf<ToDoItemListener>()

    private var data: MutableList<TodoItem> = mutableListOf()

    fun addToList(toDoItem: TodoItem){
        data.add(toDoItem)
        notifyChanges()
    }

    fun updateFromList(toDoItem: TodoItem){
        val index = data.indexOfFirst { it.id == toDoItem.id }
        data[index] = toDoItem
        notifyChanges()
    }

    fun getItemFromListById(id: String?): TodoItem?{
        return data.find { it.id == id }
    }

    fun removeFromList(toDoItem: TodoItem){
        data.removeIf { it.id == toDoItem.id }
        notifyChanges()
    }

    fun getList(): List<TodoItem> = data

    fun setList(list: List<TodoItem>){
        data = list.toMutableList()
        notifyChanges()

    }

    fun addListener(listener: ToDoItemListener){
        listeners.add(listener)
        listener.invoke(getList())
    }

    fun removeListener(listener: ToDoItemListener){
        listeners.remove(listener)
    }

    private fun notifyChanges(){
        listeners.forEach{it.invoke(getList())}
    }

}