package com.romiusse.todoapp.todo_list


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.romiusse.todoapp.todo_list.TodoItem.Companion.TABLE_NAME
import java.util.Date


@Entity(
    tableName = TABLE_NAME
)
data class TodoItem(
    @PrimaryKey
    var id: String,
    var text: String,
    var priority: PriorityItem,
    var flag: Boolean,
    var deadline: Date?,
    var createdAt: Date,
    var changedAt: Date,
){
    companion object{
        const val TABLE_NAME = "TodoItem"

        const val ID = "id"

    }
}

