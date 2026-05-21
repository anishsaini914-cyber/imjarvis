package com.jarvis.assistant.domain.model

data class LocalModelInfo(val name: String, val path: String, val size: Long = 0, val format: String = "gguf", val contextLength: Int = 2048, val temperature: Double = 0.7)
