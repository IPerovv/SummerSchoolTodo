package com.example.todoapplication.data.remote

import com.example.todoapplication.data.local.PreferencesManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 Interceptor class with "Bearer " .header
 */
@Singleton
class TodoInterceptorBearer @Inject constructor(
    private val preferencesManager: PreferencesManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
            .newBuilder()
            .addHeader("X-Last-Known-Revision", preferencesManager.getCurrentRevision().toString())
            .addHeader("Authorization", "Bearer ${preferencesManager.getBearer()}")
            .build()
        return chain.proceed(request)
    }
}
