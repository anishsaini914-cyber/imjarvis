package com.jarvis.assistant.domain.usecase

import com.jarvis.assistant.domain.model.Message
import com.jarvis.assistant.domain.repository.ChatRepositoryInterface
import javax.inject.Inject

class SendMessage @Inject constructor(private val chatRepository: ChatRepositoryInterface) {
    suspend operator fun invoke(conversationId: Long, content: String, provider: String, model: String): Result<Message> =
        chatRepository.sendMessage(conversationId, content, provider, model)
}
