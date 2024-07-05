package com.example.todoapplication.after_reg.domain.repository

import com.example.todoapplication.core.util.Resource
import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {
    fun getAllTodoItems(): Flow<Resource<List<TodoItem>>>

    fun addTodoItem(todoItem: TodoItemEntity)//Todo: реализовать

    fun updateTodoItem(todoItem: TodoItemEntity)//Todo: реализовать

    fun deleteTodoItem(todoItem: TodoItemEntity) //Todo: реализовать

    suspend fun getTodoItemById(id: String): TodoItem
}
// воркменеджер для обработки отложенной после закрытия приложения

// в фоне но пока живо приложение - foreground service

//