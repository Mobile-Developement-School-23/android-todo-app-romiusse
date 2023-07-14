package com.romiusse.todo_repository


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.romiusse.todo_repository.TodoItem.Companion.TABLE_NAME
import java.util.Calendar
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
    var notifyTime: Date? = null
){
    companion object{
        const val TABLE_NAME = "TodoItem"

        const val ID = "id"

    }

    override fun equals(other: Any?): Boolean {
        other as TodoItem
        return this.id == other.id
    }
}

