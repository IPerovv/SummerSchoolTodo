package com.example.todoapplication.after_reg.data.local

import androidx.room.*
import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDatabase(jobsList: List<TodoItemEntity>)

    @Query("SELECT * FROM todo_db")
    fun getAllTodoItems(): Flow<List<TodoItemEntity>>

    @Query("SELECT * FROM todo_db WHERE id = :id")
    suspend fun getTodoItemById(id: String): TodoItemEntity

//    @Query("SELECT * FROM job_db WHERE id = :idToFind")
//    suspend fun getJobById(idToFind: Int): Job

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTodoItem(job: TodoItemEntity)

    @Delete
    fun deleteTodoItem(job: TodoItemEntity)

}