package com.example.todoapplication.after_reg.domain.use_case

import com.example.todoapplication.core.util.Resource
import com.example.todoapplication.after_reg.domain.repository.TodoItemsRepository
import com.example.todoapplication.after_reg.domain.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllTodoItemsUseCase @Inject constructor(
    private val repository: TodoItemsRepository
) {
    operator fun invoke(): Flow<Resource<List<TodoItem>>> {
        return repository.getAllTodoItems().flowOn(Dispatchers.IO)
    }
}