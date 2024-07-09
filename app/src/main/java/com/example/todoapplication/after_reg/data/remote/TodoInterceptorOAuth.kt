package com.example.todoapplication.after_reg.data.remote

import com.example.todoapplication.after_reg.data.local.PreferencesManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
Interceptor class with "OAuth " .header
 */

@Singleton
class TodoInterceptorOAuth @Inject constructor(
    private val preferencesManager: PreferencesManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
            .newBuilder()
            .addHeader("X-Last-Known-Revision", preferencesManager.getCurrentRevision().toString())
            .addHeader("Authorization", "OAuth ${preferencesManager.getOAuthToken()}")
            .build()
        return chain.proceed(request)
    }
}
