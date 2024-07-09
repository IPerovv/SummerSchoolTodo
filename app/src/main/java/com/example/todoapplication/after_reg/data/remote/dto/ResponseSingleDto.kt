package com.example.todoapplication.after_reg.data.remote.dto


data class ResponseSingleDto(
    val status: String,
    val element: TodoItemDto,
    val revision: Int
)

