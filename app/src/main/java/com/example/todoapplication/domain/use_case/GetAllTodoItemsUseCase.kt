package com.example.todoapplication.domain.use_case

import com.example.todoapplication.core.util.Resource
import com.example.todoapplication.domain.model.TodoItem
import com.example.todoapplication.domain.repository.TodoItemsRepository
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
