package com.jarvis.assistant.domain.model

data class Conversation(val id: Long = 0, val title: String, val provider: String = "", val model: String = "", val createdAt: Long = System.currentTimeMillis(), val updatedAt: Long = System.currentTimeMillis())
