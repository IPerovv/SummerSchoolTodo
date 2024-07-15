package com.example.todoapplication.domain.use_case

import com.example.todoapplication.data.local.entity.TodoItemEntity
import com.example.todoapplication.domain.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddTodoItemUseCase @Inject constructor(
    private val repository: TodoItemsRepository
) {
    suspend operator fun invoke(job: TodoItemEntity) {
        return withContext(Dispatchers.IO) {
            repository.addTodoItem(job)
        }
    }
}
