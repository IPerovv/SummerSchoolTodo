package com.example.todoapplication.after_reg.features.todo.detailed

import androidx.lifecycle.ViewModel
import com.example.todoapplication.after_reg.domain.model.ImportanceLevel
import com.example.todoapplication.after_reg.domain.model.TodoItem
import com.example.todoapplication.after_reg.domain.use_case.GetTodoItemByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DetailedTodoItemViewModel @Inject constructor(
    private val getTodoItemByIdUseCase: GetTodoItemByIdUseCase
) : ViewModel() {

    private val _selectedImportance = MutableStateFlow(" ")
    val selectedImportance: MutableStateFlow<String> get() = _selectedImportance

    private val _deadline = MutableStateFlow<String?>(" ")
    val deadline: MutableStateFlow<String?> get() = _deadline

    private val _todoBody = MutableStateFlow(" ")
    val todoBody: StateFlow<String> get() = _todoBody

    private val _todoItem = MutableStateFlow<TodoItem?>(null)
    val todoItem: StateFlow<TodoItem?> get() = _todoItem //TODO: для последующего сохранения данных


    suspend fun setUpInfo(id: String?) {
        if (id != "null") {
            _todoItem.value = getTodoItemByIdUseCase(id!!)
            _selectedImportance.value = _todoItem.value!!.importance.name.lowercase()
            _deadline.value = setDate(_todoItem.value!!.deadline)
            _todoBody.value = _todoItem.value!!.todo
        } else {
            _selectedImportance.value =
                ImportanceLevel.basic.name.lowercase()
            _deadline.value = setDate(null)
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

    fun setDate(date: Date?): String {
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
        return when (date) {
            null -> " "
            else -> dateFormat.format(date)
        }
    }


    fun updateTodoBody(body: String) {
        _todoBody.value = body
    }
}