package com.example.todoapplication.after_reg.domain.use_case

import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.domain.repository.TodoItemsRepository

class AddTodoItem(
    private val repository: TodoItemsRepository
) {
    operator fun invoke(job: TodoItemEntity){
        return repository.addTodoItem(job)
    }
}
