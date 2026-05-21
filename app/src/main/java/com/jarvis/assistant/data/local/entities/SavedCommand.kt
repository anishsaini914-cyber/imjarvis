package com.jarvis.assistant.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_commands")
data class SavedCommand(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val command: String,
    val action: String,
    val createdAt: Long = System.currentTimeMillis()
)
