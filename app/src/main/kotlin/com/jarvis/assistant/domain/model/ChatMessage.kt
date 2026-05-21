package com.jarvis.assistant.domain.model

data class ChatMessage(val id: Long = 0, val sessionId: Long = 0, val role: String, val content: String, val timestamp: Long = System.currentTimeMillis()) {
    val isUser get() = role == "user"
    val isAssistant get() = role == "assistant"
}
