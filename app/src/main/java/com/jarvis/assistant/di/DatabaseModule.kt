package com.jarvis.assistant.di

import android.content.Context
import androidx.room.Room
import com.jarvis.assistant.data.local.AppDatabase
import com.jarvis.assistant.data.local.dao.MessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "jarvis_database").fallbackToDestructiveMigration().build()

    @Provides @Singleton
    fun provideMessageDao(database: AppDatabase): MessageDao = database.messageDao()
}
