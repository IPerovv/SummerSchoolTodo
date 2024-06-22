package com.example.todoapplication.after_reg.data.remote

import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.data.remote.dto.ResponseDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoItemsApi {
    @GET("todos")
    suspend fun getAllTodoItems(): ResponseDto

    @POST("todos/add")
    suspend fun addTodoItem(job: TodoItemEntity)

    @PUT("todos/{id}")
    suspend fun updateTodoItem(@Path("id") jobId: Int, job: TodoItemEntity)

    @DELETE("todos/{id}") //TODO
    suspend fun deleteTodoItem(@Path("id") jobId: Int)

    companion object {
        const val BASE_URL = "https://dummyjson.com/"
    }
}