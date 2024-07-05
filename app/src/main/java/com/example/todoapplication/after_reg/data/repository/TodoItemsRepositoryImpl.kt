package com.example.todoapplication.after_reg.data.repository

import android.util.Log
import com.example.todoapplication.core.util.Resource
import com.example.todoapplication.after_reg.data.local.TodoItemsDao
import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.data.local.PreferencesManager
import com.example.todoapplication.after_reg.domain.repository.TodoItemsRepository
import com.example.todoapplication.after_reg.domain.model.TodoItem
import com.example.todoapplication.after_reg.data.remote.TodoItemsApi
import com.example.todoapplication.after_reg.data.remote.dto.RequestDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException


class TodoItemsRepositoryImpl(
    private val api: TodoItemsApi,
    private val dao: TodoItemsDao,
    private val preferencesManager: PreferencesManager,
) : TodoItemsRepository {

    override fun getAllTodoItems(): Flow<Resource<List<TodoItem>>> = flow {
        emit(Resource.Loading())

        val todoItems: List<TodoItem> = runBlocking {
            dao.getAllTodoItems().map { it.map { item -> item.toTodoItem() } }.first()
        }

        emit(Resource.Loading(todoItems))

        runCatching {
            val remoteTodoItems = api.getAllTodoItems()
            dao.clearTodos()
            dao.updateDatabase(remoteTodoItems.list.map { it.toJobEntity() })
            preferencesManager.updateCurrentRevision(remoteTodoItems.revision)

        }.onFailure {

            when (it) {
                is HttpException -> emit(
                    Resource.Error(
                        message = "Oops, something went wrong!",
                        data = todoItems
                    )
                )

                is IOException -> emit(
                    Resource.Error(
                        message = "Couldn't reach server, check your internet connection.",
                        data = todoItems
                    )
                )

                else -> emit(
                    Resource.Error(
                        message = it.message.toString(),

                        data = todoItems
                    )
                )
            }
        }.onSuccess {
            val newTodos: List<TodoItem> = runBlocking {
                dao.getAllTodoItems().map { it.map { item -> item.toTodoItem() } }.first()
            }

            emit(Resource.Success(newTodos))
        }
    }

    override suspend fun addTodoItem(todoItem: TodoItemEntity) {
        dao.addTodoItem(todoItem)
        runCatching {
            val response = api.addTodoItem(RequestDto(todoItem.toTodoDto()))
            Log.i("replImpla", response.revision.toString())
        }.onFailure {
            Log.e("repImpl", "???${it.message.toString()}")
        }.onSuccess {
            Log.i("repImpl", "Successfully addedTodo")
        }
    }

    override suspend fun updateTodoItem(todoItem: TodoItemEntity) {
        dao.addTodoItem(todoItem)
        runCatching {
            val response = api.updateTodoItem(todoItem.id, RequestDto(todoItem.toTodoDto()))
        }.onFailure {
            Log.i("repImpl", "asssssssssso")
        }.onSuccess {
            Log.i("repImpl", "ssssssssssssssssso")
        }
        Log.i("repImpl", "Updated Todo")
    }

    override suspend fun deleteTodoItem(todoItem: TodoItemEntity) {
        dao.deleteTodoItem(todoItem)
        runCatching {
            val response = api.deleteTodoItem(todoItem.id)
        }.onFailure {
            Log.e("repImpl", "???${it.message.toString()}")
        }.onSuccess {

        }
        Log.i("repImpl", "{ \" ${todoItem.text} \" was deleted}")
    }

    override suspend fun getTodoItemById(id: String): TodoItem {
        return dao.getTodoItemById(id).toTodoItem()
    }

    override suspend fun updateData() {
        runCatching {
            val remoteTodoItems = api.getAllTodoItems()
            dao.clearTodos()
            dao.updateDatabase(remoteTodoItems.list.map { it.toJobEntity() })
            preferencesManager.updateCurrentRevision(remoteTodoItems.revision)
        }.onFailure {
            Log.i("repImpl", "Cant update info due to ${it.message.toString()}")
        }.onSuccess {
            Log.i("repImpl", "Data updated by worker")
        }
    }

    //    override fun getAllTodoItemsFromLocalDb(): Flow<List<TodoItem>> = flow {
//        val todoItems: List<TodoItem> = runBlocking {
//            dao.getAllTodoItems().map { it.map { item -> item.toTodoItem() } }.first()
//        }

//        emit(todoItems)
//    }

}
