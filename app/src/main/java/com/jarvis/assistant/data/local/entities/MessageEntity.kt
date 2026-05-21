package com.jarvis.assistant.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val conversationId: Long,
    val content: String,
    val role: String,
    val provider: String,
    val model: String,
    val timestamp: Long = System.currentTimeMillis()
)
