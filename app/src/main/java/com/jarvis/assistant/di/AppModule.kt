package com.jarvis.assistant.di

import android.content.Context
import com.jarvis.assistant.data.storage.PreferencesManager
import com.jarvis.assistant.data.storage.SecureStorage
import com.jarvis.assistant.util.AppCommandExecutor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager = PreferencesManager(context)
    @Provides @Singleton
    fun provideSecureStorage(@ApplicationContext context: Context): SecureStorage = SecureStorage(context)
    @Provides @Singleton
    fun provideAppCommandExecutor(@ApplicationContext context: Context): AppCommandExecutor = AppCommandExecutor(context)
}
