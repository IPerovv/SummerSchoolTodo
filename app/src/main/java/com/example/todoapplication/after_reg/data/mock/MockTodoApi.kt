package com.example.todoapplication.after_reg.data.mock

import android.content.Context
import com.example.todoapplication.after_reg.data.remote.dto.ResponseDto
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockTodoApi @Inject constructor(@ApplicationContext val context: Context) {
    fun getAllTodoItems(): ResponseDto {
        val jsonString = JsonUtil.readJsonFromFile(context, "mock1.json")
        val gson = Gson()
        return gson.fromJson(jsonString, ResponseDto::class.java)
    }
}