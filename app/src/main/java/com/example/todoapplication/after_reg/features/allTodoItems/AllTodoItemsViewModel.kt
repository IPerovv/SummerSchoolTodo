package com.example.todoapplication.after_reg.features.allTodoItems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplication.core.util.Resource
import com.example.todoapplication.after_reg.domain.use_case.GetTodoItems
import com.example.todoapplication.after_reg.presentation.TodoItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllTodoItemsViewModel @Inject constructor(
    private val getTodoItems: GetTodoItems
) : ViewModel() {

    private val _state = MutableStateFlow(TodoItemState())
    val state: StateFlow<TodoItemState> = _state.asStateFlow()

    // TODO: Дотянуть eventFlow в фрагмент
    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun loadAllTodoItems() {
        viewModelScope.launch {
            getTodoItems().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            todoItemItems = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }

                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            todoItemItems = result.data ?: emptyList(),
                            isLoading = false
                        )
                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(
                                result.message ?: "Unknown error"
                            )
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            todoItemItems = result.data ?: emptyList(),
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }

}