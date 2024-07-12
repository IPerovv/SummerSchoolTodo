package com.example.todoapplication.after_reg.data.remote.dto

import com.google.gson.annotations.SerializedName


data class RequestSingleDto(
    @SerializedName("element")
    val element: TodoItemDto,
)

