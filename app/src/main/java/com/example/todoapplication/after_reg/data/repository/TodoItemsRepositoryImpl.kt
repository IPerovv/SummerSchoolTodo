package com.example.todoapplication.after_reg.data.repository

import android.util.Log
import com.example.todoapplication.core.util.Resource
import com.example.todoapplication.after_reg.data.local.TodoItemsDao
import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.domain.repository.TodoItemsRepository
import com.example.todoapplication.after_reg.domain.model.TodoItem
import com.example.todoapplication.after_reg.data.remote.TodoItemsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException

class TodoItemsRepositoryImpl(
    private val api: TodoItemsApi,
    private val dao: TodoItemsDao
) : TodoItemsRepository {

    override fun getAllTodoItems(): Flow<Resource<List<TodoItem>>> = flow {
        emit(Resource.Loading())

        val todoItems = dao.getAllTodoItems().map { it.map { item -> item.toTodoItem() } }
        val unwrappedTodoItems: List<TodoItem> = runBlocking { todoItems.first() }

        emit(Resource.Loading(unwrappedTodoItems))

        runCatching {
            val remoteTodoItems = api.getAllTodoItems()
            dao.updateDatabase(remoteTodoItems.todos.map { it.toJobEntity() })

        }.onFailure {
            when (it) {
                is HttpException -> emit(
                    Resource.Error(
                        message = "Oops, something went wrong!",
                        data = unwrappedTodoItems
                    )
                )

                is IOException -> emit(
                    Resource.Error(
                        message = "Couldn't reach server, check your internet connection.",
                        data = unwrappedTodoItems
                    )
                )

                else -> emit(
                    Resource.Error(
                        message = it.message.toString(),
                        data = unwrappedTodoItems
                    )
                )
            }
        }.onSuccess {
            val newJobs = dao.getAllTodoItems().map { it.map { item -> item.toTodoItem() } }
            val newUnwrappedTodoItems: List<TodoItem> = runBlocking { newJobs.first() }
            emit(Resource.Success(newUnwrappedTodoItems))
        }

    }

    //TODO
    override fun addTodoItem(todoItem: TodoItemEntity) {
        runCatching {
            dao.addTodoItem(todoItem)
        }.onFailure {

        }.onSuccess {

        }
        Log.i("repImpl", "Successfully addedTodo")
    }

    override fun updateTodoItem(todoItem: TodoItemEntity){
        TODO("Not yet implemented")
    }


    // TODO: Убрать try
    override fun deleteTodoItem(todoItem: TodoItemEntity){
        try {
            dao.deleteTodoItem(todoItem)
        } catch (e: HttpException) {
            Log.e("RepImpl", "Smth went wrong")
        } catch (e: IOException) {
            Log.e("RepImpl", "Couldn't reach server")
        }
        Log.i("repImpl", "{ \" ${todoItem.todo} \" was deleted}")
    }

    override suspend fun getTodoItemById(id: String): TodoItem {
           return dao.getTodoItemById(id).toTodoItem()
    }

}
