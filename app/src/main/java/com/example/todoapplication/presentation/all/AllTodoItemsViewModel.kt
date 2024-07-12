package com.example.todoapplication.presentation.all

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplication.core.util.Resource
import com.example.todoapplication.domain.model.TodoItem
import com.example.todoapplication.domain.use_case.GetAllTodoItemsUseCase
import com.example.todoapplication.domain.use_case.UpdateDataAfterConnectionLossUseCase
import com.example.todoapplication.domain.use_case.UpdateTodoItemUseCase
import com.example.todoapplication.domain.ConnectivityObserver
import com.example.todoapplication.presentation.TodoItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllTodoItemsViewModel @Inject constructor(
    private val getAllTodoItemsUseCase: GetAllTodoItemsUseCase,
    private val updateDataAfterConnectionLossUseCase: UpdateDataAfterConnectionLossUseCase,
    private val updateTodoItemUseCase: UpdateTodoItemUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(TodoItemState())
    val state: StateFlow<TodoItemState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _connectionFlow = MutableSharedFlow<ConnectivityObserver.ConnectionStatus>()
    val connectionFlow = _connectionFlow.asSharedFlow()

    init {
        loadAllTodoItems()
        observeConnection()
    }

    fun loadAllTodoItems() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllTodoItemsUseCase().collect { result ->
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

    fun updateTodoItem(toDoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTodoItemUseCase(toDoItem.toTodoItemEntity())
        }
    }

    fun syncDataAfterConnectionLoss() {
        viewModelScope.launch(Dispatchers.IO) {
            updateDataAfterConnectionLossUseCase().collect {
                _state.value = state.value.copy(
                    todoItemItems = it,
                    isLoading = false
                )
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }

    private fun observeConnection() {
        connectivityObserver.observe()
            .onEach { status ->
                _connectionFlow.emit(status)
            }.launchIn(viewModelScope)
    }
}


