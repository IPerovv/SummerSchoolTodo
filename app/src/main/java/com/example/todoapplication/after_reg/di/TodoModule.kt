package com.example.todoapplication.after_reg.di

import android.content.Context
import androidx.room.Room
import com.example.todoapplication.after_reg.data.local.TodoItemsDao
import com.example.todoapplication.after_reg.domain.use_case.AddTodoItem
import com.example.todoapplication.after_reg.domain.use_case.UpdateTodoItem
import com.example.todoapplication.after_reg.data.local.TodoItemsDatabase
import com.example.todoapplication.after_reg.data.repository.MockTodoItemsRepositoryImpl
import com.example.todoapplication.after_reg.data.mock.MockTodoApi
import com.example.todoapplication.after_reg.domain.repository.TodoItemsRepository
import com.example.todoapplication.after_reg.data.remote.TodoItemsApi
import com.example.todoapplication.after_reg.domain.use_case.GetTodoItemById
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TodoModule {

    @Provides
    @Singleton
    fun providesAddTodoUseCase(repository: TodoItemsRepository): AddTodoItem {
        return AddTodoItem(repository)
    }

    @Provides
    @Singleton
    fun providesGetTodoItemById(repository: TodoItemsRepository): GetTodoItemById{
        return GetTodoItemById(repository)
    }

    @Provides
    @Singleton
    fun providesUpdateTodoUseCase(repository: TodoItemsRepository): UpdateTodoItem {
        return UpdateTodoItem(repository)
    }


    @Provides
    @Singleton
    fun providesTodoInfoDatabase(@ApplicationContext appContext: Context): TodoItemsDatabase {
        return Room.databaseBuilder(
            appContext,
            TodoItemsDatabase::class.java,
            "todo_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(db: TodoItemsDatabase): TodoItemsDao = db.dao

    @Provides
    @Singleton
    fun providesTodoItemsRepository(
        db: TodoItemsDatabase,
        api: TodoItemsApi,
        mockApi: MockTodoApi,
    ): TodoItemsRepository {
        //return JobsRepositoryImpl(api, db.dao)
        return MockTodoItemsRepositoryImpl(mockApi, db.dao)
    }

    @Provides
    @Singleton
    fun providesTodoApi(): TodoItemsApi {
        return Retrofit.Builder().baseUrl(TodoItemsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoItemsApi::class.java)
    }

}


