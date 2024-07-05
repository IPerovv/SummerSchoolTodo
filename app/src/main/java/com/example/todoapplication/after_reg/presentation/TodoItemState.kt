package com.example.todoapplication.after_reg.presentation

import com.example.todoapplication.after_reg.domain.model.TodoItem

data class TodoItemState (
    val todoItemItems: List<TodoItem> = emptyList(),
    val isLoading: Boolean = false
)
