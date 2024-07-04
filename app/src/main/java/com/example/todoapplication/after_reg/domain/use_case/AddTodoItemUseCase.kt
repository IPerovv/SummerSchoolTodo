package com.example.todoapplication.after_reg.domain.use_case

import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.domain.repository.TodoItemsRepository
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
