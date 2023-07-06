package com.romiusse.todo_repository.room

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
