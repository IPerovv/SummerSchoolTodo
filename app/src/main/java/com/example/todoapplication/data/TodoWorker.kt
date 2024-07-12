package com.example.todoapplication.data

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapplication.domain.use_case.UpdateDataUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


/**
    Worker class that update info every 8 hours
 */
@HiltWorker
class TodoWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val updateDataUseCase: UpdateDataUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            updateDataUseCase
            Log.i("TodoWorker", "Work completed successfully")
            Result.success()
        } catch (exception: Exception) {
            Log.e("TodoWorker", "Work failed", exception)
            Result.failure()
        }
    }
}
