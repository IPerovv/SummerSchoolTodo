package com.example.todoapplication.data.remote.dto

data class ResponseDto(
    val status: String,
    val list: List<TodoItemDto>,
    val revision: Int
)

