package com.example.todoapplication.after_reg.data.remote.dto

import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.domain.model.ImportanceLevel
import java.util.Date

data class TodoItemDto(
    val id: String,
    val todo: String,
    val importance: ImportanceLevel,
    val completed: Boolean,
    val creationDate: Date,
    val modificationDate : Date,
    val deadline: Date? = null
) {
    fun toJobEntity(): TodoItemEntity {
        return TodoItemEntity(
            id, todo, importance, completed, creationDate, modificationDate, deadline
        )
    }
}