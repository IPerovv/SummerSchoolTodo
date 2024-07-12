package com.example.todoapplication.domain

interface StringProvider {
    fun getString(resId: Int): String
}