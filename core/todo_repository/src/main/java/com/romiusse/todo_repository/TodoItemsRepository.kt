package com.romiusse.todo_repository


import com.romiusse.todo_repository.room.TodoItemDao
import kotlinx.coroutines.flow.Flow

class TodoItemsRepository(private val todoItemDao: TodoItemDao): TodoItemsRepositoryInterface{

    override suspend fun addToList(toDoItem: TodoItem){
        todoItemDao.addToList(toDoItem)
    }

    override suspend fun updateFromList(toDoItem: TodoItem){
        todoItemDao.updateFromList(toDoItem)
    }

    override suspend fun getItemFromListById(id: String): TodoItem? {
        return todoItemDao.getItemFromListById(id)
    }

    override suspend fun removeFromList(toDoItem: TodoItem){
        todoItemDao.removeFromList(toDoItem)
    }

    override suspend fun updateAllList(toDoItems: List<TodoItem>) {
        todoItemDao.updateAllList(toDoItems)
    }

    override fun getList(): Flow<List<TodoItem>>{
        return todoItemDao.getList()
    }


}