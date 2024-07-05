package com.example.todoapplication

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.todoapplication.after_reg.data.worker.TodoWorker
import com.example.todoapplication.after_reg.domain.repository.TodoItemsRepository
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class JobsApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: TodoWorkerFactory

    override fun onCreate() {
        super.onCreate()
        initBackgroundTodoWorker()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    class TodoWorkerFactory @Inject constructor(
        private val todoItemsRepository: TodoItemsRepository
    ): WorkerFactory(){
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker = TodoWorker(appContext, workerParameters, todoItemsRepository)
    }

    fun initBackgroundTodoWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<TodoWorker>(
            8, TimeUnit.HOURS
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "TodoWorker",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
    }
}