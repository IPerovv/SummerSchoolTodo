package com.example.todoapplication.data.remote

import com.example.todoapplication.data.remote.dto.RequestDto
import com.example.todoapplication.data.remote.dto.RequestSingleDto
import com.example.todoapplication.data.remote.dto.ResponseDto
import com.example.todoapplication.data.remote.dto.ResponseSingleDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoItemsApi {
    @GET("list")
    suspend fun getAllTodoItems(): ResponseDto

    @POST("list")
    suspend fun addTodoItem(@Body requestSingleDto: RequestSingleDto): ResponseSingleDto

    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Path("id") todoId: String,
        @Body requestSingleDto: RequestSingleDto
    ): ResponseSingleDto

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(@Path("id") todoId: String): ResponseSingleDto

    @PATCH("list")
    suspend fun patchServerInfo(@Body requestDto: RequestDto): ResponseDto
}
