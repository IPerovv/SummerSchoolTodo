package com.example.todoapplication.after_reg.data.remote.dto


data class ResponseDto(
    val status: String,
    val list: List<TodoItemDto>,
    val revision: Int
)

