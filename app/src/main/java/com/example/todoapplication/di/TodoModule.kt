package com.example.todoapplication.di

import android.content.Context
import androidx.room.Room
import com.example.todoapplication.data.local.PreferencesManager
import com.example.todoapplication.data.local.TodoItemsDao
import com.example.todoapplication.data.local.TodoItemsDatabase
import com.example.todoapplication.data.remote.TodoInterceptorBearer
import com.example.todoapplication.data.remote.TodoInterceptorOAuth
import com.example.todoapplication.data.remote.TodoItemsApi
import com.example.todoapplication.data.repository.TodoItemsRepositoryImpl
import com.example.todoapplication.data.stringProvider.StringProvider
import com.example.todoapplication.data.stringProvider.StringProviderImpl
import com.example.todoapplication.domain.repository.TodoItemsRepository
import com.example.todoapplication.features.connectivity.ConnectivityObserver
import com.example.todoapplication.features.connectivity.NetworkKConnectivityObserver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TodoModule {

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
        stringProvider: StringProvider
        //mockApi: MockTodoApi,
    ): TodoItemsRepository {
        return TodoItemsRepositoryImpl(api, db.dao, preferencesManager, stringProvider)
        // return MockTodoItemsRepositoryImpl(mockApi, db.dao)
    }

    @Provides
    @Singleton
    fun providesTodoApi(
        client: OkHttpClient,
        preferencesManager: PreferencesManager
    ): TodoItemsApi {
        return Retrofit.Builder()
            .baseUrl(preferencesManager.getBaseUrl())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoItemsApi::class.java)
    }

    @Singleton
    @Provides
    fun providesOkhttpClient(
        todoInterceptorOAuth: TodoInterceptorOAuth,
        todoInterceptorBearer: TodoInterceptorBearer //Менять тут если нет OAuth
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .apply { addInterceptor(todoInterceptorOAuth) }.build()
            //.apply { addInterceptor(todoInterceptorBearer) }.build()
    }

    @Provides
    @Singleton
    fun provideStringProvider(@ApplicationContext context: Context): StringProvider {
        return StringProviderImpl(context)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class ConnectivityModule {
        @Binds
        abstract fun bindConnectivityObserver(
            networkKConnectivityObserver: NetworkKConnectivityObserver
        ): ConnectivityObserver
    }

//    @Provides
//    @Singleton
//    fun providesTodoInterceptorBearer(
//        preferencesManager: PreferencesManager
//    ): TodoInterceptorBearer {
//        return TodoInterceptorBearer(
//            BuildConfig.AUTH_PASSWORD,
//            preferencesManager.getCurrentRevision()
//        )
//    }
}


