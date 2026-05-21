package com.jarvis.assistant.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jarvis.assistant.data.local.dao.ChatSessionDao
import com.jarvis.assistant.data.local.dao.MessageDao
import com.jarvis.assistant.data.local.entity.ChatSessionEntity
import com.jarvis.assistant.data.local.entity.MessageEntity

@Database(entities = [MessageEntity::class, ChatSessionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun chatSessionDao(): ChatSessionDao
}
