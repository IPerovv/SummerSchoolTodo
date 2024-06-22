package com.example.todoapplication.after_reg.data.repository

import android.util.Log
import com.example.todoapplication.core.util.Resource
import com.example.todoapplication.after_reg.data.local.TodoItemsDao
import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.domain.repository.TodoItemsRepository
import com.example.todoapplication.after_reg.domain.model.TodoItem
import com.example.todoapplication.after_reg.data.remote.TodoItemsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class TodoItemsRepositoryImpl(
    private val api: TodoItemsApi,
    private val dao: TodoItemsDao
) : TodoItemsRepository {

    override fun getAllTodoItems(): Flow<Resource<List<TodoItem>>> = flow {
        emit(Resource.Loading())

        val todoItems = dao.getAllTodoItems().map { it.toTodoItem() }
        emit(Resource.Loading(todoItems))

        runCatching {
            val remoteTodoItems = api.getAllTodoItems()
            dao.updateDatabase(remoteTodoItems.todos.map { it.toJobEntity() })

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
                        message = it.toString(),
                        data = todoItems
                    )
                )
            }
        }.onSuccess {
            val newJobs = dao.getAllTodoItems().map { it.toTodoItem() }
            emit(Resource.Success(newJobs))
        }

    }

    //TODO
    override fun addTodoItem(job: TodoItemEntity) {
        runCatching {
            dao.addTodoItem(job)
        }.onFailure {

        }.onSuccess {

        }
        Log.i("repImpl", "Successfully addedTodo")
    }

    override fun updateTodoItem(job: TodoItemEntity){
        TODO("Not yet implemented")
    }


    // TODO: Убрать try
    override fun deleteTodoItem(job: TodoItemEntity){
        try {
            dao.deleteTodoItem(job)
        } catch (e: HttpException) {
            Log.e("RepImpl", "Smth went wrong")
        } catch (e: IOException) {
            Log.e("RepImpl", "Couldn't reach server")
        }
        Log.i("repImpl", "{ \" ${job.todo} \" was deleted}")
    }

    override suspend fun getTodoItemById(id: String): TodoItem {
           return dao.getTodoItemById(id).toTodoItem()
    }

}
