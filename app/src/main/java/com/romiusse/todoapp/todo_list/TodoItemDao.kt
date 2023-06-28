package com.romiusse.todoapp.todo_list

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {

    @Insert
    suspend fun addToList(todoItem: TodoItem)

    @Query("SELECT * FROM ${TodoItem.TABLE_NAME}")
    fun getList() : Flow<List<TodoItem>>

    @Update()
    suspend fun updateFromList(toDoItem: TodoItem)

    @Query("SELECT * FROM ${TodoItem.TABLE_NAME} WHERE ${TodoItem.ID} = :id")
    suspend fun getItemFromListById(id: String): TodoItem?

    @Delete()
    suspend fun removeFromList(toDoItem: TodoItem)

}