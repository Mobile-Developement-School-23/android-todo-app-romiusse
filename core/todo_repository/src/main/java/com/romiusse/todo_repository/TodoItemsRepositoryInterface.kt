package com.romiusse.todo_repository

import kotlinx.coroutines.flow.Flow

interface TodoItemsRepositoryInterface {


    suspend fun addToList(toDoItem: TodoItem)

    suspend fun updateFromList(toDoItem: TodoItem)

    suspend fun getItemFromListById(id: String): TodoItem?

    suspend fun removeFromList(toDoItem: TodoItem)

    suspend fun updateAllList(toDoItems: List<TodoItem>)

    fun getList(): Flow<List<TodoItem>>

}