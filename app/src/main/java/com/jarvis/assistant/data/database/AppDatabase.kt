package com.jarvis.assistant.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jarvis.assistant.domain.model.ChatMessage

@Database(entities = [ChatMessage::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
}
