package com.example.todoapplication.after_reg.data.repository

import android.util.Log
import com.example.todoapplication.R
import com.example.todoapplication.after_reg.data.local.PreferencesManager
import com.example.todoapplication.after_reg.data.local.TodoItemsDao
import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.data.remote.TodoItemsApi
import com.example.todoapplication.after_reg.data.remote.dto.RequestDto
import com.example.todoapplication.after_reg.data.remote.dto.RequestSingleDto
import com.example.todoapplication.after_reg.data.stringProvider.StringProvider
import com.example.todoapplication.after_reg.domain.model.TodoItem
import com.example.todoapplication.after_reg.domain.repository.TodoItemsRepository
import com.example.todoapplication.core.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException

/**
repository implementation
 */
class TodoItemsRepositoryImpl(
    private val api: TodoItemsApi,
    private val dao: TodoItemsDao,
    private val preferencesManager: PreferencesManager,
    private val stringProvider: StringProvider
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
            dao.updateDatabase(remoteTodoItems.list.map { it.toTodoEntity() })
            preferencesManager.updateCurrentRevision(remoteTodoItems.revision)

        }.onFailure {
            when (it) {
                is HttpException -> {
                    val errorMessage = when (it.code()) {
                        400 -> stringProvider.getString(R.string.http400Error)
                        401 -> stringProvider.getString(R.string.http401Error)
                        404 -> stringProvider.getString(R.string.http404Error)
                        500 -> stringProvider.getString(R.string.http500Error)
                        else -> "Oops, something went wrong!"
                    }
                    emit(
                        Resource.Error(
                            message = errorMessage.toString(),
                            data = todoItems
                        )
                    )
                }

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

    override suspend fun addTodoItem(todoItem: TodoItemEntity){
        dao.addTodoItem(todoItem)
        runCatching {
            val response = api.addTodoItem(RequestSingleDto(todoItem.toTodoDto()))
            preferencesManager.updateCurrentRevision(response.revision)
        }.onFailure {
            Log.e("repImpl", "???${it.message.toString()}")
        }.onSuccess {

        }
    }

    override suspend fun updateTodoItem(todoItem: TodoItemEntity)  {
        dao.addTodoItem(todoItem)
        runCatching {
            val response = api.updateTodoItem(todoItem.id, RequestSingleDto(todoItem.toTodoDto()))
            preferencesManager.updateCurrentRevision(response.revision)
        }.onFailure {
            Log.i("repImpl", "asssssssssso")
        }
        Log.i("repImpl", "Updated Todo")
        //Тут когда-то появятся уведомления ошибок
    }

    override suspend fun deleteTodoItem(todoItem: TodoItemEntity) {
        dao.deleteTodoItem(todoItem)
        runCatching {
            val response = api.deleteTodoItem(todoItem.id)
            preferencesManager.updateCurrentRevision(response.revision)
        }.onFailure {
            Log.e("repImpl", "???${it.message.toString()}")
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
            dao.updateDatabase(remoteTodoItems.list.map { it.toTodoEntity() })
            preferencesManager.updateCurrentRevision(remoteTodoItems.revision)
        }.onFailure {
            Log.i("repImpl", "Cant update info due to ${it.message.toString()}")
        }.onSuccess {
            Log.i("repImpl", "Data updated by worker")
        }
    }

    override suspend fun updateDataAfterConnectionLoss(): Flow<List<TodoItem>> = flow {
        runCatching {
            val remoteData = api.getAllTodoItems()
            val remoteTodoItems = remoteData.list.map { it.toTodoEntity() }
            preferencesManager.updateCurrentRevision(remoteData.revision)

            val localTodoItems = dao.getAllTodoItems().first()

            // Create maps of items by their ID for both local and remote items
            val remoteItemsMap = remoteTodoItems.associateBy { it.id }
            val localItemsMap = localTodoItems.associateBy { it.id }

            // Create a set of all unique IDs from both local and remote items
            val allIds = localItemsMap.keys.union(remoteItemsMap.keys)

            // Create a list to store the merged items
            val mergedItems = mutableListOf<TodoItemEntity>()

            // Iterate through all unique IDs
            for (id in allIds) {
                val localItem = localItemsMap[id]
                val remoteItem = remoteItemsMap[id]

                // Determine which item to keep based on modification date
                val itemToKeep = when {
                    localItem == null -> null
                    remoteItem == null -> localItem // Only exists in local
                    localItem.modificationDate?.after(remoteItem.modificationDate) == true -> localItem // Local is newer
                    else -> remoteItem // Remote is newer or they are the same
                }

                // Add the determined item to the merged items list
                itemToKeep?.let { mergedItems.add(it) }
            }

            val afterPatch = api.patchServerInfo(RequestDto(mergedItems.map { it.toTodoDto() }))

            dao.updateDatabase(afterPatch.list.map { it.toTodoEntity() })
            preferencesManager.updateCurrentRevision(afterPatch.revision)

        }.onFailure {
            Log.i("repImpl", "Cant merge and update due to ${it.message.toString()}")
        }.onSuccess {

        }
    }
}
