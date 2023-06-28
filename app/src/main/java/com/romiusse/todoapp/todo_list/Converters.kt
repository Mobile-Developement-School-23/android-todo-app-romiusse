package com.romiusse.todoapp.todo_list

import androidx.room.TypeConverter
import java.util.Date


class Converters {

    @TypeConverter
    fun from(data: Date?): Long? {
        return data?.time
    }

    @TypeConverter
    fun to(time: Long?): Date? {
        return if(time != null) Date(time) else null
    }

}