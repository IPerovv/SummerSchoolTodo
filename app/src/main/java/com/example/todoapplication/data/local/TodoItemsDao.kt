package com.example.todoapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todoapplication.data.local.entity.TodoItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDatabase(jobsList: List<TodoItemEntity>)

    @Query("SELECT * FROM todo_db")
    fun getAllTodoItems(): Flow<List<TodoItemEntity>>

    @Query("SELECT * FROM todo_db WHERE id = :id")
    suspend fun getTodoItemById(id: String): TodoItemEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodoItem(job: TodoItemEntity)

    @Delete
    suspend fun deleteTodoItem(job: TodoItemEntity)

    @Query("DELETE FROM todo_db")
    suspend fun clearTodos()

}
