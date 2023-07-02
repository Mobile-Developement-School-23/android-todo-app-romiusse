package com.romiusse.todoapp.todo_list.room

import android.provider.SyncStateContract.Helpers.insert
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.romiusse.todoapp.todo_list.TodoItem
import kotlinx.coroutines.flow.Flow
import java.nio.file.Files.delete


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