package com.example.todoapplication.data.remote.dto

import com.google.gson.annotations.SerializedName


data class RequestDto(
    @SerializedName("list")
    val list: List<TodoItemDto>,
)

