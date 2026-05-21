package com.jarvis.assistant.data.repository

import com.jarvis.assistant.data.local.dao.ChatDao
import com.jarvis.assistant.data.local.dao.MessageDao
import com.jarvis.assistant.data.local.entity.ChatEntity
import com.jarvis.assistant.data.local.entity.MessageEntity
import com.jarvis.assistant.domain.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao
) {
    fun getAllChats(): Flow<List<ChatEntity>> = chatDao.getAllChats()
    suspend fun getChatById(id: Long): ChatEntity? = chatDao.getChatById(id)
    suspend fun createChat(title: String = "New Chat", provider: String = "openai", model: String = "gpt-4o-mini"): Long =
        chatDao.insertChat(ChatEntity(title = title, provider = provider, model = model))
    suspend fun deleteChat(id: Long) = chatDao.deleteChatById(id)
    suspend fun updateChatTitle(id: Long, title: String) = chatDao.updateChatTitle(id, title)
    suspend fun updateChatTimestamp(id: Long) = chatDao.updateChatTimestamp(id, System.currentTimeMillis())

    fun getMessagesByChatId(chatId: Long): Flow<List<Message>> =
        messageDao.getMessagesByChatId(chatId).map { list -> list.map { it.toDomain() } }

    suspend fun getMessagesByChatIdSync(chatId: Long): List<Message> =
        messageDao.getMessagesByChatIdSync(chatId).map { it.toDomain() }

    suspend fun addMessage(chatId: Long, role: String, content: String): Long =
        messageDao.insertMessage(MessageEntity(chatId = chatId, role = role, content = content))

    suspend fun clearChat(chatId: Long) = messageDao.deleteMessagesByChatId(chatId)

    private fun MessageEntity.toDomain() = Message(id = id, chatId = chatId, role = role, content = content, timestamp = timestamp, isLoading = isLoading)
}
