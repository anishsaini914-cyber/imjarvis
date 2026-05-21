package com.jarvis.assistant.di

import com.jarvis.assistant.data.repository.ChatRepository
import com.jarvis.assistant.data.repository.SettingsRepository
import com.jarvis.assistant.data.repository.WeatherRepository
import com.jarvis.assistant.domain.repository.ChatRepositoryInterface
import com.jarvis.assistant.domain.repository.SettingsRepositoryInterface
import com.jarvis.assistant.domain.repository.WeatherRepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindChatRepository(impl: ChatRepository): ChatRepositoryInterface
    @Binds @Singleton abstract fun bindSettingsRepository(impl: SettingsRepository): SettingsRepositoryInterface
    @Binds @Singleton abstract fun bindWeatherRepository(impl: WeatherRepository): WeatherRepositoryInterface
}
