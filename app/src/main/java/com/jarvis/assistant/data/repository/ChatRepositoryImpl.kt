package com.jarvis.assistant.data.repository

import com.jarvis.assistant.data.database.ChatMessageDao
import com.jarvis.assistant.domain.model.ChatMessage
import com.jarvis.assistant.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val chatMessageDao: ChatMessageDao
) : ChatRepository {
    override fun getAllMessages(): Flow<List<ChatMessage>> = chatMessageDao.getAllMessages()
    override suspend fun insertMessage(message: ChatMessage) = chatMessageDao.insertMessage(message)
    override suspend fun deleteMessage(message: ChatMessage) = chatMessageDao.deleteMessage(message)
    override suspend fun clearAll() = chatMessageDao.clearAll()
}
