package com.example.todoapplication.after_reg.domain.use_case

import com.example.todoapplication.after_reg.domain.model.TodoItem
import com.example.todoapplication.after_reg.domain.repository.TodoItemsRepository

class GetTodoItemById(
    private val repository: TodoItemsRepository
) {
    suspend operator fun invoke(id: String) : TodoItem {
        return repository.getTodoItemById(id)
    }
}