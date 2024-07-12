package com.example.todoapplication.data.remote.dto


data class ResponseSingleDto(
    val status: String,
    val element: TodoItemDto,
    val revision: Int
)

