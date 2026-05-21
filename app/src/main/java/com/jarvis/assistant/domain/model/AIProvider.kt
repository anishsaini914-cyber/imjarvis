package com.jarvis.assistant.domain.model

data class AiProvider(val id: String, val name: String, val description: String, val defaultModel: String, val models: List<String> = emptyList(), val requiresApiKey: Boolean = true, val isLocalModel: Boolean = false) {
    companion object {
        val OPENAI = AiProvider("openai", "OpenAI", "GPT-4o and more", "gpt-4o-mini", listOf("gpt-4o-mini", "gpt-4o", "gpt-4-turbo"))
        val GEMINI = AiProvider("gemini", "Gemini", "Google Gemini models", "gemini-2.0-flash", listOf("gemini-2.0-flash", "gemini-1.5-pro"))
        val AGENT_ROUTER = AiProvider("agentrouter", "AgentRouter", "Multi-model router", "gpt-4o-mini", listOf("gpt-4o-mini", "gpt-4o", "claude-3-haiku"))
        val LOCAL = AiProvider("local", "Local Model", "On-device GGUF/GGML", "", requiresApiKey = false, isLocalModel = true)
        fun getAll(): List<AiProvider> = listOf(OPENAI, GEMINI, AGENT_ROUTER, LOCAL)
    }
}
