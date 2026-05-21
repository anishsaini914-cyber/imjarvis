package com.jarvis.assistant.domain.repository

import com.jarvis.assistant.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getAllMessages(): Flow<List<ChatMessage>>
    suspend fun insertMessage(message: ChatMessage)
    suspend fun deleteMessage(message: ChatMessage)
    suspend fun clearAll()
}
