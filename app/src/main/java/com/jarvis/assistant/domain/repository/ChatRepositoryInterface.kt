package com.jarvis.assistant.domain.repository

import com.jarvis.assistant.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepositoryInterface {
    fun getMessages(conversationId: Long): Flow<List<Message>>
    suspend fun sendMessage(conversationId: Long, content: String, provider: String, model: String): Result<Message>
    suspend fun saveMessage(message: Message): Long
    suspend fun deleteConversation(conversationId: Long)
}
