package com.example.todoapplication.after_reg.data.remote.dto

import com.example.todoapplication.after_reg.data.local.entity.TodoItemEntity
import com.example.todoapplication.after_reg.domain.model.ImportanceLevel
import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.util.Date

data class TodoItemDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("importance")
    val importance: String,
    @SerializedName("deadline")
    val deadline: Long? = null,
    @SerializedName("done")
    val done: Boolean,
    @SerializedName("color")
    val color: String? = null,
    @SerializedName("created_at")
    val creationDate: Long,
    @SerializedName("changed_at")
    val modificationDate: Long?,
    @SerializedName("last_updated_by")
    val lastUpdatedBy: String?
) {
    fun toJobEntity(): TodoItemEntity {
        return TodoItemEntity(
            id = id,
            text = text,
            importance = ImportanceLevel.valueOf(importance.uppercase()),
            done = done,
            creationDate = convertTimestampToDate(creationDate) ?: Date(),
            modificationDate = convertTimestampToDate(modificationDate),
            deadline = convertTimestampToDate(deadline),
            lastUpdatedBy = lastUpdatedBy
        )
    }

    private fun convertTimestampToDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it * 1000) }
    }
}