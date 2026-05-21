package com.jarvis.assistant.domain.model

data class Message(val id: Long = 0, val chatId: Long, val role: String, val content: String, val timestamp: Long = System.currentTimeMillis(), val isLoading: Boolean = false) {
    val isUser: Boolean get() = role == "user"
    val isAssistant: Boolean get() = role == "assistant"
}
