package com.romiusse.todoapp.todo_list

import kotlinx.coroutines.flow.Flow

interface TodoItemsRepositoryInterface {


    suspend fun addToList(toDoItem: TodoItem)

    suspend fun updateFromList(toDoItem: TodoItem)

    suspend fun getItemFromListById(id: String): TodoItem?

    suspend fun removeFromList(toDoItem: TodoItem)

    suspend fun updateAllList(toDoItems: List<TodoItem>)

    suspend fun deleteAll()

    fun getList(): Flow<List<TodoItem>>

}