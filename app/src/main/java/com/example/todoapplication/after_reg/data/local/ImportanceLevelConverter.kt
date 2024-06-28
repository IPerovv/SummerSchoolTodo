package com.example.todoapplication.after_reg.data.local

import androidx.room.TypeConverter
import com.example.todoapplication.after_reg.domain.model.ImportanceLevel

class ImportanceLevelConverter {
    @TypeConverter
    fun fromImportanceLevel(importanceLevel: ImportanceLevel): String {
        return importanceLevel.name
    }

    @TypeConverter
    fun fromImportanceLevel(importanceLevel: String): ImportanceLevel {
        return enumValueOf(importanceLevel)
    }
}