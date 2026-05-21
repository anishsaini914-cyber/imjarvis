package com.jarvis.assistant.data.repository

import com.jarvis.assistant.data.local.dao.ChatSessionDao
import com.jarvis.assistant.data.local.dao.MessageDao
import com.jarvis.assistant.data.local.entity.ChatSessionEntity
import com.jarvis.assistant.data.local.entity.MessageEntity
import com.jarvis.assistant.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val messageDao: MessageDao,
    private val sessionDao: ChatSessionDao
) {
    fun getMessages(sessionId: Long): Flow<List<ChatMessage>> =
        messageDao.getMessagesBySession(sessionId).map { list -> list.map { it.toDomain() } }

    fun getAllSessions(): Flow<List<ChatSessionEntity>> = sessionDao.getAllSessions()

    suspend fun createSession(title: String = "New Chat", provider: String = "openai", model: String = ""): Long =
        sessionDao.insertSession(ChatSessionEntity(title = title, provider = provider, model = model))

    suspend fun saveMessage(msg: ChatMessage): Long =
        messageDao.insertMessage(MessageEntity(sessionId = msg.sessionId, role = msg.role, content = msg.content, timestamp = msg.timestamp))

    suspend fun deleteSession(session: ChatSessionEntity) = sessionDao.deleteSession(session)
    suspend fun deleteMessages(sessionId: Long) = messageDao.deleteMessagesBySession(sessionId)

    private fun MessageEntity.toDomain() = ChatMessage(id = id, sessionId = sessionId, role = role, content = content, timestamp = timestamp)
}
