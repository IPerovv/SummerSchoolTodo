package com.example.todoapplication.after_reg.data.remote

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class TodoInterceptor(
    username: String,
    private val password: String,
    private val revision: Int
) : Interceptor {
   //private val credentials: String = Credentials.basic(username, password)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
            .newBuilder()
            .addHeader("X-Last-Known-Revision", revision.toString())
            .addHeader("Authorization", "Bearer $password")
            .build()
        return chain.proceed(request)
    }
}