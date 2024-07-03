package com.example.todoapplication.after_reg.data.remote.dto

import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.domain.model.ImportanceLevel
import java.util.Date

data class TodoItemDto(
    val id: String,
    val text: String,
    val importance: ImportanceLevel,
    val deadline: Long? = null,
    val done: Boolean,
    val color: String? = null,
    val creationDate: Long,
    val modificationDate: Long,
    val lastUpdatedBy: String
) {
    fun toJobEntity(): TodoItemEntity {
        return TodoItemEntity(
            id = id,
            text = text,
            importance = importance,
            done = done,
            creationDate = Date(creationDate * 1000),
            modificationDate = Date(creationDate * 1000),
            deadline = Date(creationDate * 1000)
        )
    }
}