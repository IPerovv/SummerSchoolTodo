package com.example.todoapplication.after_reg.domain.repository

import com.example.todoapplication.core.util.Resource
import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {
    fun getAllTodoItems(): Flow<Resource<List<TodoItem>>>

    suspend fun addTodoItem(todoItem: TodoItemEntity)

    suspend fun updateTodoItem(todoItem: TodoItemEntity)

    suspend fun deleteTodoItem(todoItem: TodoItemEntity)

    suspend fun getTodoItemById(id: String): TodoItem

    suspend fun updateData()
}