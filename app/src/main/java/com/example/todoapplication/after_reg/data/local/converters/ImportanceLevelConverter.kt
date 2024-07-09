package com.example.todoapplication.after_reg.data.local.converters

import androidx.room.TypeConverter
import com.example.todoapplication.after_reg.domain.model.ImportanceLevel

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
