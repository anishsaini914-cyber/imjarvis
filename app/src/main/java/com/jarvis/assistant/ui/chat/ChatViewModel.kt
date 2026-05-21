package com.jarvis.assistant.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.assistant.data.storage.PreferencesManager
import com.jarvis.assistant.domain.model.Message
import com.jarvis.assistant.domain.repository.ChatRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepositoryInterface,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> = _messages
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error
    private val currentConversationId = 1L

    fun loadMessages() { viewModelScope.launch { chatRepository.getMessages(currentConversationId).collect { _messages.postValue(it) } } }

    fun sendMessage(text: String) {
        val provider = preferencesManager.selectedProvider
        val model = when (provider) { "openai" -> preferencesManager.openAiModel; "gemini" -> preferencesManager.geminiModel; "agentrouter" -> preferencesManager.agentRouterModel; else -> preferencesManager.openAiModel }
        viewModelScope.launch {
            _isLoading.value = true; _error.value = null
            chatRepository.sendMessage(currentConversationId, text, provider, model).onFailure { _error.value = "Error: ${it.message}" }
            _isLoading.value = false
        }
    }
}
