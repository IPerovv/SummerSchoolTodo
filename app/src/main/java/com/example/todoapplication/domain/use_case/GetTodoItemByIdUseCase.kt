package com.example.todoapplication.domain.use_case

import com.example.todoapplication.domain.model.TodoItem
import com.example.todoapplication.domain.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTodoItemByIdUseCase @Inject constructor(
    private val repository: TodoItemsRepository
) {
    suspend operator fun invoke(id: String): TodoItem {
        return withContext(Dispatchers.IO) {
            repository.getTodoItemById(id)
        }
    }
}
