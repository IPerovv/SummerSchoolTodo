package com.example.todoapplication.after_reg.di

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.todoapplication.BuildConfig
import com.example.todoapplication.after_reg.data.remote.TodoInterceptor
import com.example.todoapplication.after_reg.data.local.TodoItemsDao
import com.example.todoapplication.after_reg.domain.use_case.AddTodoItemUseCase
import com.example.todoapplication.after_reg.domain.use_case.UpdateTodoItemUseCase
import com.example.todoapplication.after_reg.data.local.TodoItemsDatabase
import com.example.todoapplication.after_reg.data.mock.MockTodoApi
import com.example.todoapplication.after_reg.data.remote.PreferencesManager
import com.example.todoapplication.after_reg.domain.repository.TodoItemsRepository
import com.example.todoapplication.after_reg.data.remote.TodoItemsApi
import com.example.todoapplication.after_reg.data.repository.TodoItemsRepositoryImpl
import com.example.todoapplication.after_reg.domain.use_case.GetTodoItemByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TodoModule {

    @Provides
    fun providesAddTodoUseCase(repository: TodoItemsRepository): AddTodoItemUseCase {
        return AddTodoItemUseCase(repository)
    }

    @Provides
    fun providesGetTodoItemById(repository: TodoItemsRepository): GetTodoItemByIdUseCase {
        return GetTodoItemByIdUseCase(repository)
    }

    @Provides
    fun providesUpdateTodoUseCase(repository: TodoItemsRepository): UpdateTodoItemUseCase {
        return UpdateTodoItemUseCase(repository)
    }


    @Provides
    @Singleton
    fun providesTodoInfoDatabase(
        @ApplicationContext appContext: Context
    ): TodoItemsDatabase {
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
        preferencesManager: PreferencesManager,
        mockApi: MockTodoApi,
    ): TodoItemsRepository {
        return TodoItemsRepositoryImpl(api, db.dao, preferencesManager)
        // return MockTodoItemsRepositoryImpl(mockApi, db.dao)
    }

    @Provides
    @Singleton
    fun providesTodoApi(
        client: OkHttpClient
    ): TodoItemsApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoItemsApi::class.java)
    }

    @Singleton
    @Provides
    fun providesOkhttpClient(
        todoInterceptor: TodoInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .apply { addInterceptor(todoInterceptor) }.build()
    }

    @Provides
    @Singleton
    fun providesPreferencesManager(
        @ApplicationContext context: Context
    ): PreferencesManager {
        return PreferencesManager(context)
    }

    @Provides
    @Singleton
    fun providesTodoInterceptor(
        preferencesManager: PreferencesManager
    ): TodoInterceptor {
        return TodoInterceptor(
            BuildConfig.AUTH_LOGIN,
            BuildConfig.AUTH_PASSWORD,
            preferencesManager.getCurrentRevision()
        )
    }

}


