package com.example.todoapplication.after_reg.data.local.converters

import androidx.room.TypeConverter
import java.util.Date

/**
    typeConverter class date-timestamp
*/
class DateConverter {
    @TypeConverter
    fun timestampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}
