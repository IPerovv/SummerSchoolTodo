package com.example.todoapplication.domain.model



import com.example.todoapplication.data.local.entity.TodoItemEntity
import java.util.Date

data class TodoItem(
    val id: String,
    val todo: String,
    val importance: ImportanceLevel,
    val completed: Boolean,
    val creationDate: Date,
    val modificationDate: Date?,
    val deadline: Date?,
    val lastUpdatedBy: String?,
    val files: List<String>?
) {

    fun toTodoItemEntity(): TodoItemEntity {
        return TodoItemEntity(
            id = id,
            text = todo,
            importance = importance,
            done = completed,
            creationDate = creationDate,
            modificationDate = modificationDate,
            deadline = deadline,
            lastUpdatedBy = lastUpdatedBy
        )
    }
}

enum class ImportanceLevel {
    LOW,
    BASIC,
    IMPORTANT,
}
