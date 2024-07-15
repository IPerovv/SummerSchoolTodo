package com.example.todoapplication.presentation

import com.example.todoapplication.domain.model.TodoItem

/**
state class that represents state of List<TodoItem> in AllTodoItemViewModel
 */
data class TodoItemState (
    val todoItemItems: List<TodoItem> = emptyList(),
    val isLoading: Boolean = false
)
