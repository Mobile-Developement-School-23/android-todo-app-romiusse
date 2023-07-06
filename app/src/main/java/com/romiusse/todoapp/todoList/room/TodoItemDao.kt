package com.romiusse.todoapp.todoList.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.romiusse.todoapp.todoList.TodoItem
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToList(todoItem: TodoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<TodoItem>)

    @Query("SELECT * FROM ${TodoItem.TABLE_NAME}")
    fun getList() : Flow<List<TodoItem>>

    @Update()
    suspend fun updateFromList(toDoItem: TodoItem)

    @Query("SELECT * FROM ${TodoItem.TABLE_NAME} WHERE ${TodoItem.ID} = :id")
    suspend fun getItemFromListById(id: String): TodoItem?

    @Delete()
    suspend fun removeFromList(toDoItem: TodoItem)

    @Query("DELETE FROM ${TodoItem.TABLE_NAME}")
    suspend fun deleteAll()

    @Transaction
    suspend fun updateAllList(list: List<TodoItem>) {
        deleteAll()
        insertAll(list)
    }

}