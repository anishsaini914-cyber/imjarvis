package com.jarvis.assistant.domain.repository

import com.jarvis.assistant.domain.model.AIProvider
import kotlinx.coroutines.flow.Flow

interface AIRepository {
    suspend fun sendMessage(message: String, provider: AIProvider, model: String): Result<String>
    suspend fun sendMessageStream(message: String, provider: AIProvider, model: String): Result<Flow<String>>
}
