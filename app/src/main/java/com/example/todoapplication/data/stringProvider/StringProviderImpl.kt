package com.example.todoapplication.data.stringProvider

import android.content.Context

class StringProviderImpl(private val context: Context) : StringProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}