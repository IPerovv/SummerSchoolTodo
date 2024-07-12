package com.example.todoapplication.data

import android.content.Context
import com.example.todoapplication.domain.StringProvider

class StringProviderImpl(private val context: Context) : StringProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}