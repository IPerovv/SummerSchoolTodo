package com.example.todoapplication.data.mock

import android.content.Context

object JsonUtil {
    fun readJsonFromFile(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}
