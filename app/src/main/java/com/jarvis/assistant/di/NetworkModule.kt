package com.jarvis.assistant.di

import com.jarvis.assistant.data.remote.api.AgentRouterApi
import com.jarvis.assistant.data.remote.api.GeminiApi
import com.jarvis.assistant.data.remote.api.OpenAiApi
import com.jarvis.assistant.data.remote.api.WeatherApi
import com.jarvis.assistant.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(logging).connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build()
    }

    @Provides @Singleton
    fun provideOpenAiApi(client: OkHttpClient): OpenAiApi =
        Retrofit.Builder().baseUrl("https://api.openai.com/").client(client).addConverterFactory(GsonConverterFactory.create()).build().create(OpenAiApi::class.java)

    @Provides @Singleton
    fun provideGeminiApi(client: OkHttpClient): GeminiApi =
        Retrofit.Builder().baseUrl("https://generativelanguage.googleapis.com/").client(client).addConverterFactory(GsonConverterFactory.create()).build().create(GeminiApi::class.java)

    @Provides @Singleton
    fun provideAgentRouterApi(client: OkHttpClient): AgentRouterApi =
        Retrofit.Builder().baseUrl(Constants.AGENT_ROUTER_BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(AgentRouterApi::class.java)

    @Provides @Singleton
    fun provideWeatherApi(client: OkHttpClient): WeatherApi =
        Retrofit.Builder().baseUrl(Constants.OPEN_WEATHER_BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(WeatherApi::class.java)
}
