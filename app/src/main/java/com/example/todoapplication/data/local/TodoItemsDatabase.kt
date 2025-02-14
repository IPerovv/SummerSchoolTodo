package com.example.todoapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapplication.data.local.converters.DateConverter
import com.example.todoapplication.data.local.converters.ImportanceLevelConverter
import com.example.todoapplication.data.local.entity.TodoItemEntity

@Database(
    entities = [TodoItemEntity::class],
    version = 1
)
@TypeConverters(DateConverter::class, ImportanceLevelConverter::class)
abstract class TodoItemsDatabase: RoomDatabase() {
    abstract val dao: TodoItemsDao
}
