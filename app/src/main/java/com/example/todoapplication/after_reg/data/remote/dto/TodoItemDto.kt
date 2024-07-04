package com.example.todoapplication.after_reg.data.remote.dto

import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.domain.model.ImportanceLevel
import java.sql.Timestamp
import java.util.Date

data class TodoItemDto(
    val id: String,
    val text: String,
    val importance: String,
    val deadline: Long? = null,
    val done: Boolean,
    val color: String? = null,
    val creationDate: Long,
    val modificationDate: Long?,
    val lastUpdatedBy: String
) {
    fun toJobEntity(): TodoItemEntity {
        return TodoItemEntity(
            id = id,
            text = text,
            importance = ImportanceLevel.valueOf(importance.uppercase()),
            done = done,
            creationDate = convertTimestampToDate(creationDate) ?: Date(),
            modificationDate = convertTimestampToDate(modificationDate),
            deadline = convertTimestampToDate(deadline)
        )
    }

    private fun convertTimestampToDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it * 1000) }
    }
}