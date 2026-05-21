package com.jarvis.assistant.domain.model

enum class AiProvider(val id: String, val display: String) {
    OPEN_AI("openai", "OpenAI"), GEMINI("gemini", "Gemini"), AGENT_ROUTER("agentrouter", "AgentRouter");
    companion object { fun fromId(id: String) = entries.firstOrNull { it.id == id } ?: OPEN_AI }
}
