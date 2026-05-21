package com.jarvis.assistant.data.remote.dto

data class AgentRouterRequest(val model: String, val messages: List<AgentRouterMessage>, val temperature: Double = 0.7, val max_tokens: Int = 2000)
data class AgentRouterMessage(val role: String, val content: String)
data class AgentRouterResponse(val choices: List<AgentRouterChoice>?, val error: String?)
data class AgentRouterChoice(val message: AgentRouterMessage, val finish_reason: String?)
