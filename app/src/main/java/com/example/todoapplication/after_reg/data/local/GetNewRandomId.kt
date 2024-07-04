package com.example.todoapplication.after_reg.data.local

import java.util.UUID

fun getNewRandomId(): String {
    return UUID.randomUUID().toString()
}
