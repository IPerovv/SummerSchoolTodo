package com.example.todoapplication.data.local.converters

import androidx.room.TypeConverter
import com.example.todoapplication.domain.model.ImportanceLevel

/**
typeConverter class string-importanceLevel
 */
class ImportanceLevelConverter {
    @TypeConverter
    fun fromImportanceLevel(importanceLevel: ImportanceLevel): String {
        return importanceLevel.name.uppercase()
    }

    @TypeConverter
    fun toImportanceLevel(importanceLevel: String): ImportanceLevel {
        return enumValueOf(importanceLevel)
    }

}
