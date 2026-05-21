package com.jarvis.assistant.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jarvis.assistant.data.local.entity.ChatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY updatedAt DESC")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chats WHERE id = :id")
    suspend fun getChatById(id: Long): ChatEntity?

    @Insert
    suspend fun insertChat(chat: ChatEntity): Long

    @Update
    suspend fun updateChat(chat: ChatEntity)

    @Delete
    suspend fun deleteChat(chat: ChatEntity)

    @Query("DELETE FROM chats WHERE id = :id")
    suspend fun deleteChatById(id: Long)

    @Query("UPDATE chats SET title = :title WHERE id = :id")
    suspend fun updateChatTitle(id: Long, title: String)

    @Query("UPDATE chats SET updatedAt = :ts WHERE id = :id")
    suspend fun updateChatTimestamp(id: Long, ts: Long)
}
