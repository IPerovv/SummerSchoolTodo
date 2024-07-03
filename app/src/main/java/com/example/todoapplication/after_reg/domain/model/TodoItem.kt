package com.example.todoapplication.after_reg.domain.model


import java.util.Date

data class TodoItem(
    val id: String,
    val todo: String,
    val importance: ImportanceLevel,
    val completed: Boolean,
    val creationDate: Date,
    val modificationDate : Date,
    val deadline: Date?
)
enum class ImportanceLevel {
    low,
    basic,
    important,
}