package com.example.todoapplication.core.util

import java.util.UUID

fun getNewRandomId(): String {
    return UUID.randomUUID().toString()
}
