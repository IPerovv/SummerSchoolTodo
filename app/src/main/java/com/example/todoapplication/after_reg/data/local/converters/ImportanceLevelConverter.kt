package com.example.todoapplication.after_reg.data.local.converters

import androidx.compose.ui.text.toUpperCase
import androidx.room.TypeConverter
import com.example.todoapplication.after_reg.domain.model.ImportanceLevel
import java.util.Locale

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