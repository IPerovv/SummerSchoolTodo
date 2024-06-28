package com.example.todoapplication.after_reg.features.todo.detailed

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.example.todoapplication.R
import com.example.todoapplication.after_reg.domain.model.ImportanceLevel
import com.example.todoapplication.after_reg.domain.model.TodoItem
import com.example.todoapplication.after_reg.domain.use_case.GetTodoItemById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
open class DetailedTodoItemViewModel @Inject constructor(
    private val getTodoItemById: GetTodoItemById
) : ViewModel() {

    val _selectedImportance = MutableStateFlow<String>(" ")
    val selectedImportance: MutableStateFlow<String> get() = _selectedImportance

    val _deadline = MutableStateFlow<String?>(" ")
    val deadline: MutableStateFlow<String?> get() = _deadline

    val _todoBody = MutableStateFlow<String>(" ")
    val todoBody: MutableStateFlow<String> get() = _todoBody

    private val _todoItem = MutableStateFlow<TodoItem?>(null)
    val todoItem: StateFlow<TodoItem?> get() = _todoItem //TODO: для последующего сохранения данных


    suspend fun setUpInfo(id: String?, resources: Resources) {
        if (id != "null") {
            _todoItem.value = getTodoItemById(id!!)
            _selectedImportance.value =
                resources.getString(setImportance(_todoItem.value!!.importance))
            _deadline.value = setDate(_todoItem.value!!.deadline)
            _todoBody.value = _todoItem.value!!.todo
        } else {
            _selectedImportance.value =
                resources.getString(setImportance(ImportanceLevel.BASIC))
            _deadline.value = setDate(null)
            _todoBody.value = ""
        }
    }

    private fun setImportance(importance: ImportanceLevel): Int {
        return when (importance) {
            ImportanceLevel.LOW -> R.string.importance_low
            ImportanceLevel.BASIC -> R.string.importance_basic
            ImportanceLevel.IMPORTANT -> R.string.importance_high
        }
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

    fun setupPopupMenu(menuItem: Int, resources: Resources) {
        when (menuItem) {
            R.id.importance_low -> _selectedImportance.value =
                resources.getString(setImportance(ImportanceLevel.LOW))

            R.id.importance_basic -> _selectedImportance.value =
                resources.getString(setImportance(ImportanceLevel.BASIC))

            R.id.importance_high -> _selectedImportance.value =
                resources.getString(setImportance(ImportanceLevel.IMPORTANT))
        }
    }
    fun updateTodoBody(body: String) {
        _todoBody.value = body
    }
}