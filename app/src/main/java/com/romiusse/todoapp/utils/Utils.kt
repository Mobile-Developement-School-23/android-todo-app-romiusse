package com.romiusse.todoapp.utils

import com.romiusse.todoapp.server.model.ServerTodoItem
import com.romiusse.todoapp.todoList.PriorityItem
import com.romiusse.todoapp.todoList.TodoItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {


    fun convertDateToString(date: Date?): String{
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(date)
    }


    fun convertPosToPriority(positionId: Int): PriorityItem{
        return when(positionId){
            0 -> PriorityItem.LOW
            1 -> PriorityItem.BASE
            2 -> PriorityItem.HIGH
            else -> PriorityItem.LOW
        }
    }

    fun getPosFromPriority(priority: PriorityItem): Int{
        return when(priority){
            PriorityItem.LOW -> 0
            PriorityItem.BASE -> 1
            PriorityItem.HIGH -> 2
        }
    }

    fun convertServerModelToClient(model: ServerTodoItem): TodoItem =
        TodoItem(
            id = model.id,
            text = model.text,
            priority = when(model.importance){
                "low" -> PriorityItem.LOW
                "basic" -> PriorityItem.BASE
                "important" -> PriorityItem.HIGH
                else -> PriorityItem.BASE
            },
            flag = model.done,
            deadline = model.deadline?.let{Date(it)},
            createdAt = Date(model.createdAt),
            changedAt = Date(model.changedAt)
        )

    fun convertClientModelToServer(model: TodoItem): ServerTodoItem =
        ServerTodoItem(
            id = model.id,
            text = model.text,
            importance = when(model.priority){
                PriorityItem.LOW -> "low"
                PriorityItem.BASE -> "basic"
                PriorityItem.HIGH -> "important"
            },
            deadline = model.deadline?.time,
            done = model.flag,
            color = null,
            createdAt = model.createdAt.time,
            changedAt =  model.changedAt.time,
            lastUpdatedBy = android.os.Build.MODEL
        )


}