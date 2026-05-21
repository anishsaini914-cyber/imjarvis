package com.jarvis.assistant.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jarvis.assistant.data.local.dao.ChatDao
import com.jarvis.assistant.data.local.dao.MessageDao
import com.jarvis.assistant.data.local.entity.ChatEntity
import com.jarvis.assistant.data.local.entity.MessageEntity

@Database(entities = [ChatEntity::class, MessageEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(ctx: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java, "jarvis_database").build().also { INSTANCE = it }
        }
    }
}
