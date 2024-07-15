package com.example.todoapplication.data.repository

import android.util.Log
import com.example.todoapplication.core.util.Resource
import com.example.todoapplication.data.local.TodoItemsDao
import com.example.todoapplication.data.local.entity.TodoItemEntity
import com.example.todoapplication.data.mock.MockTodoApi
import com.example.todoapplication.domain.model.TodoItem
import com.example.todoapplication.domain.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 deprecated repository implementation for mock data
 */
class MockTodoItemsRepositoryImpl(
    private val mock: MockTodoApi,
    private val dao: TodoItemsDao
) : TodoItemsRepository {
    override fun getAllTodoItems(): Flow<Resource<List<TodoItem>>> = flow {
        emit(Resource.Loading())

        val todoItems: List<TodoItem> = runBlocking {
            dao.getAllTodoItems().map { it.map { item -> item.toTodoItem() } }.first()
        }

        emit(Resource.Loading(todoItems))

        runCatching {
            val mockTodo = mock.getAllTodoItems()
            dao.updateDatabase(mockTodo.list.map { it.toTodoEntity() })

        }.onFailure {
            Resource.Error(
                message = "Something wrong with mock - ${it.message}",
                data = todoItems
            )
        }.onSuccess {
            val newTodoItems: List<TodoItem> = runBlocking {
                dao.getAllTodoItems().map { it.map { item -> item.toTodoItem() } }.first()
            }
            emit(Resource.Success(newTodoItems))
        }.getOrNull()
    }.flowOn(Dispatchers.IO)

    //exception handler corutines + getOrThrow
    override suspend fun addTodoItem(todoItem: TodoItemEntity) {
        dao.addTodoItem(todoItem)
        runCatching {

        }.onFailure {
            TODO("Изменить тип возвращаемого объекта")
        }.onSuccess {
            Log.i("repImpl", "Successfully updated info")
        }.getOrNull()
    }

    override suspend fun updateData(){}

    override suspend fun updateTodoItem(todoItem: TodoItemEntity) {
        TODO()
    }

    override suspend fun deleteTodoItem(todoItem: TodoItemEntity) {
        runCatching {
            dao.deleteTodoItem(todoItem)
        }.onFailure {
            TODO("Изменить тип возвращаемого объекта")
        }.onSuccess {
            Log.i("repImpl", "{ \" ${todoItem.text} \" was deleted}")
        }.getOrNull()
    }

    override suspend fun getTodoItemById(id: String): TodoItem {
        return dao.getTodoItemById(id).toTodoItem()
    }

    override suspend fun updateDataAfterConnectionLoss(): Flow<List<TodoItem>> {
        TODO("Not yet implemented")
    }
}
