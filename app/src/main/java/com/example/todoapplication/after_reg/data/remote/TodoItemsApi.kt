package com.example.todoapplication.after_reg.data.remote

import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.data.remote.dto.RequestDto
import com.example.todoapplication.after_reg.data.remote.dto.ResponseDto
import com.example.todoapplication.after_reg.data.remote.dto.ResponseSingleDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoItemsApi {
    @GET("list")
    suspend fun getAllTodoItems(): ResponseDto

    @POST("list")
    suspend fun addTodoItem(@Body requestDto: RequestDto): ResponseSingleDto

    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Path("id") todoId: String,
        @Body requestDto: RequestDto
    ): ResponseSingleDto

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(@Path("id") todoId: String): ResponseSingleDto
}
