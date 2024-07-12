package com.example.todoapplication.data.local

import java.util.UUID

fun getNewRandomId(): String {
    return UUID.randomUUID().toString()
}
