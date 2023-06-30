package com.romiusse.todoapp.todo_list.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.romiusse.todoapp.todo_list.TodoItem

@Database(
    entities = [TodoItem::class], version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun todoItemDao(): TodoItemDao

    companion object{

        fun buildDatabase(context: Context, dbName: String): AppDatabase {
            return  Room.databaseBuilder(context, AppDatabase::class.java, dbName).build()
        }

    }

}