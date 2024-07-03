package com.example.todoapplication.after_reg.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapplication.after_reg.domain.model.ImportanceLevel
import com.example.todoapplication.after_reg.domain.model.TodoItem
import java.util.Date

@Entity(tableName = "todo_db")
data class TodoItemEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey val id: String,
    @ColumnInfo(name = "job_title")
    val text: String,
    @ColumnInfo("importance_level")
    val importance: ImportanceLevel,
    @ColumnInfo("completed_bool")
    val done: Boolean,
    @ColumnInfo("creation_date")
    val creationDate: Date,
    @ColumnInfo("modification_date")
    val modificationDate: Date,
    @ColumnInfo("deadline")
    val deadline: Date?
) {
    fun toTodoItem(): TodoItem {
        return TodoItem(
            id, text, importance, done, creationDate, modificationDate, deadline
        )
    }
}
