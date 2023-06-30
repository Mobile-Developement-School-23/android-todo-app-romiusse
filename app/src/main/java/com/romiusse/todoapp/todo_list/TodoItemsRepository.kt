package com.romiusse.todoapp.todo_list


import com.romiusse.todoapp.todo_list.room.TodoItemDao
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
        deleteAll()
        todoItemDao.insertAll(toDoItems)
    }

    override suspend fun deleteAll() {
        todoItemDao.deleteAll()
    }

    override fun getList(): Flow<List<TodoItem>>{
        return todoItemDao.getList()
    }

    //TODO Merge brute and lazy in one algorithm
    private suspend fun bruteUpdateList(toDoItems: List<TodoItem>){
        lazyUpdateList(toDoItems)
    }

    private suspend fun lazyUpdateList(toDoItems: List<TodoItem>){
        toDoItems.forEach{ todoItemDao.addToList(it) }
    }


}