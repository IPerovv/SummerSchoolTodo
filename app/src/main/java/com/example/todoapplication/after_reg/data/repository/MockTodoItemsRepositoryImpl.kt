package com.example.todoapplication.after_reg.data.repository

import android.util.Log
import com.example.todoapplication.after_reg.data.local.TodoItemsDao
import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.data.mock.MockTodoApi
import com.example.todoapplication.after_reg.domain.model.TodoItem
import com.example.todoapplication.after_reg.domain.repository.TodoItemsRepository
import com.example.todoapplication.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockTodoItemsRepositoryImpl(
    private val mock: MockTodoApi,
    private val dao: TodoItemsDao
) : TodoItemsRepository {
    override fun getAllTodoItems(): Flow<Resource<List<TodoItem>>> = flow {
        emit(Resource.Loading())

        val todoItems = dao.getAllTodoItems().map { it.toTodoItem() }
        emit(Resource.Loading(todoItems))

        runCatching {
            val mockTodo = mock.getAllTodoItems()
            dao.updateDatabase(mockTodo.todos.map { it.toJobEntity() })

        }.onFailure {
            Resource.Error(
                message = it.toString(),
                data = todoItems
            )
        }.onSuccess {
            val newJobs = dao.getAllTodoItems().map { it.toTodoItem() }
            emit(Resource.Success(newJobs))
        }.getOrNull()
    }
    //exception handler corutines + getOrThrow
    override fun addTodoItem(job: TodoItemEntity){
        runCatching {
            dao.addTodoItem(job)
        }.onFailure {
            TODO("Изменить тип возвращаемого объекта")
        }.onSuccess {
            Log.i("repImpl", "Successfully updated info")
        }.getOrNull()
    }

    override fun updateTodoItem(job: TodoItemEntity) {
    }

    override fun deleteTodoItem(job: TodoItemEntity) {
        runCatching {
            dao.deleteTodoItem(job)
        }.onFailure {
            TODO("Изменить тип возвращаемого объекта")
        }.onSuccess {
            Log.i("repImpl", "{ \" ${job.todo} \" was deleted}")
        }.getOrNull()
    }

    override suspend fun getTodoItemById(id: String): TodoItem {
        return dao.getTodoItemById(id).toTodoItem()
    }
}