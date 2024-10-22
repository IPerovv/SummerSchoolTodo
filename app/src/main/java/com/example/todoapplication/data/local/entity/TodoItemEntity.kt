package com.example.todoapplication.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapplication.data.remote.dto.TodoItemDto
import com.example.todoapplication.domain.model.ImportanceLevel
import com.example.todoapplication.domain.model.TodoItem
import java.util.Date

/**
Entity data class for todo_db
 */
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
    val modificationDate: Date?,
    @ColumnInfo("deadline")
    val deadline: Date?,
    @ColumnInfo("last_updated_by")
    val lastUpdatedBy: String?
) {
    fun toTodoItem(): TodoItem {
        return TodoItem(
            id, text, importance, done, creationDate, modificationDate, deadline, lastUpdatedBy, null
        )
    }

    fun toTodoDto(): TodoItemDto {
        return TodoItemDto(
            id = id,
            text = text,
            importance = importance.name.lowercase(),
            done = done,
            creationDate = dateToTimestamp(creationDate),
            modificationDate = dateToTimestamp(modificationDate),
            deadline = dateToTimestamp(deadline),
            lastUpdatedBy = lastUpdatedBy,
            files = null
        )
    }

    private fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.div(1000)
    }

    private fun dateToTimestamp(date: Date): Long {
        return date.time / 1000
    }

}
