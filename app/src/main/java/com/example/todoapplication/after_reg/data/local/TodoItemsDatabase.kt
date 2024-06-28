package com.example.todoapplication.after_reg.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity

@Database(
    entities = [TodoItemEntity::class],
    version = 1
)
@TypeConverters(DataConverters::class, ImportanceLevelConverter::class)
abstract class TodoItemsDatabase: RoomDatabase() {
    abstract val dao: TodoItemsDao
}