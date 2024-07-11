package com.example.todoapplication.after_reg.features.todo.detailed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplication.after_reg.data.local.PreferencesManager
import com.example.todoapplication.after_reg.data.local.getNewRandomId
import com.example.todoapplication.after_reg.domain.model.ImportanceLevel
import com.example.todoapplication.after_reg.domain.model.TodoItem
import com.example.todoapplication.after_reg.domain.use_case.AddTodoItemUseCase
import com.example.todoapplication.after_reg.domain.use_case.DeleteTodoItemUseCase
import com.example.todoapplication.after_reg.domain.use_case.GetTodoItemByIdUseCase
import com.example.todoapplication.after_reg.domain.use_case.UpdateTodoItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DetailedTodoItemViewModel @Inject constructor(
    private val getTodoItemByIdUseCase: GetTodoItemByIdUseCase,
    private val addItemUseCase: AddTodoItemUseCase,
    private val updateTodoItemUseCase: UpdateTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    preferencesManager: PreferencesManager
) : ViewModel() {

    private val _selectedImportance = MutableStateFlow(" ")
    val selectedImportance: MutableStateFlow<String> get() = _selectedImportance

    private val _deadline = MutableStateFlow<String?>(null)
    val deadline: MutableStateFlow<String?> get() = _deadline

    private val _todoBody = MutableStateFlow(" ")
    val todoBody: StateFlow<String> get() = _todoBody

    private val _todoItem = MutableStateFlow<TodoItem?>(null)
    val todoItem: StateFlow<TodoItem?> get() = _todoItem

    private val thisDeviceId = preferencesManager.getCurrentDeviceId()


    suspend fun setUpInfo(id: String?) {
        if (id != null) {
            _todoItem.value = getTodoItemByIdUseCase(id)
            _selectedImportance.value = _todoItem.value!!.importance.name.lowercase()
            _deadline.value = _todoItem.value!!.deadline?.let { setDate(it) }
            _todoBody.value = _todoItem.value!!.todo
        } else {
            _selectedImportance.value = ImportanceLevel.BASIC.name.lowercase()
            _deadline.value = null
            _todoBody.value = ""
        }
    }

    fun setImportance(importance: ImportanceLevel) {
        _selectedImportance.value = importance.name.lowercase()
    }

    fun setNewDate(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        _deadline.value = setDate(calendar.time)
    }

    private fun setDate(date: Date?): String? {
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        return when (date) {
            null -> null
            else -> dateFormat.format(date)
        }
    }

    fun clearDeadline() {
        _deadline.value = null
    }


    private fun dateFromString(date: String?): Date? {
        if (date == null) return null

        val format = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        return format.parse(date)
    }

    fun updateTodoBody(body: String) {
        _todoBody.value = body
    }

    suspend fun addTodoItem() {
        viewModelScope.launch(Dispatchers.IO) {
            addItemUseCase(
                TodoItem(
                    id = getNewRandomId(),
                    todo = _todoBody.value,
                    completed = false,
                    creationDate = Date(),
                    deadline = dateFromString(_deadline.value),
                    modificationDate = Date(),
                    importance = ImportanceLevel.valueOf(_selectedImportance.value.uppercase()),
                    lastUpdatedBy = thisDeviceId,
                    files = null
                ).toTodoItemEntity()
            )
        }.join()
    }

    suspend fun updateTodoItem() {
        viewModelScope.launch(Dispatchers.IO) {
            updateTodoItemUseCase(
                TodoItem(
                    id = _todoItem.value!!.id,
                    todo = _todoBody.value,
                    completed = _todoItem.value!!.completed,
                    creationDate = _todoItem.value!!.creationDate,
                    deadline = dateFromString(_deadline.value),
                    modificationDate = Date(),
                    importance = ImportanceLevel.valueOf(_selectedImportance.value.uppercase()),
                    lastUpdatedBy = thisDeviceId,
                    files = null
                ).toTodoItemEntity()
            )
        }.join()
    }

    suspend fun deleteTodoItem() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTodoItemUseCase(
                TodoItem(
                    id = _todoItem.value!!.id,
                    todo = _todoBody.value,
                    completed = _todoItem.value!!.completed,
                    creationDate = _todoItem.value!!.creationDate,
                    deadline = dateFromString(_deadline.value),
                    modificationDate = Date(),
                    importance = ImportanceLevel.valueOf(_selectedImportance.value.uppercase()),
                    lastUpdatedBy = thisDeviceId,
                    files = null
                ).toTodoItemEntity()
            )
        }.join()
    }

}
