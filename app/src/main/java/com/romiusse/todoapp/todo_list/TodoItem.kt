package com.romiusse.todoapp.todo_list

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TodoItem(
    var id: String,
    var text: String,
    var priority: PriorityItem,
    var flag: Boolean,
    var deadline: Date?,
    var createdAt: Date,
    var changedAt: Date,
)
