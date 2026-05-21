package com.jarvis.assistant.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jarvis.assistant.R
import com.jarvis.assistant.data.repository.AiRepository
import com.jarvis.assistant.data.repository.ChatRepository
import com.jarvis.assistant.data.repository.SettingsRepository
import com.jarvis.assistant.databinding.FragmentChatBinding
import com.jarvis.assistant.ui.adapter.MessageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private var _b: FragmentChatBinding? = null; private val b get() = _b!!
    @Inject lateinit var chatRepository: ChatRepository
    @Inject lateinit var aiRepository: AiRepository
    @Inject lateinit var settingsRepository: SettingsRepository
    private lateinit var adapter: MessageAdapter
    private var currentChatId = -1L
    private var sr: SpeechRecognizer? = null
    private var isListening = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentChatBinding.inflate(inflater, container, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MessageAdapter().also { b.messagesRecyclerView.layoutManager = LinearLayoutManager(requireContext()).apply { stackFromEnd = true }; b.messagesRecyclerView.adapter = it }
        b.btnSend.setOnClickListener { sendMessage() }
        b.btnVoiceInput.setOnClickListener { if (isListening) stopListening() else startListening() }
        lifecycleScope.launch { currentChatId = chatRepository.createChat(); observeMessages() }
    }

    private fun observeMessages() {
        chatRepository.getMessagesByChatId(currentChatId).observe(viewLifecycleOwner) { msgs ->
            adapter.submitList(msgs); val has = msgs.isNotEmpty()
            b.emptyChat.visibility = if (has) View.GONE else View.VISIBLE
            b.messagesRecyclerView.visibility = if (has) View.VISIBLE else View.GONE
            if (has) b.messagesRecyclerView.smoothScrollToPosition(msgs.size - 1)
        }
    }

    private fun sendMessage() {
        val text = b.inputMessage.text.toString().trim(); if (text.isEmpty()) return
        b.inputMessage.setText("")
        lifecycleScope.launch {
            chatRepository.addMessage(currentChatId, "user", text)
            chatRepository.updateChatTimestamp(currentChatId)
            val msgs = chatRepository.getMessagesByChatIdSync(currentChatId)
            val provider = if (settingsRepository.isLocalModelEnabled()) "local" else settingsRepository.getActiveProvider()
            val model = when (provider) { "openai" -> settingsRepository.getOpenAiModel(); "gemini" -> settingsRepository.getGeminiModel(); "agentrouter" -> settingsRepository.getAgentRouterModel(); else -> "" }
            chatRepository.addMessage(currentChatId, "assistant", "...")
            withContext(Dispatchers.IO) { aiRepository.getChatCompletion(msgs, provider, model) }
                .onSuccess { chatRepository.addMessage(currentChatId, "assistant", it) }
                .onFailure { chatRepository.addMessage(currentChatId, "assistant", "Error: ${it.message}") }
            val chat = chatRepository.getChatById(currentChatId)
            if (chat?.title == "New Chat") chatRepository.updateChatTitle(currentChatId, if (text.length > 30) "${text.take(30)}..." else text)
        }
    }

    private fun startListening() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Microphone permission required", Toast.LENGTH_SHORT).show(); return
        }
        sr = SpeechRecognizer.createSpeechRecognizer(requireContext())
        sr?.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: android.os.Bundle?) {
                results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?.let { b.inputMessage.setText(it); sendMessage() }; stopListening()
            }
            override fun onPartialResults(partialResults: android.os.Bundle?) {
                partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?.let { b.inputMessage.setText(it) }
            }
            override fun onReadyForSpeech(params: android.os.Bundle?) { isListening = true; b.btnVoiceInput.setColorFilter(ContextCompat.getColor(requireContext(), R.color.primary)) }
            override fun onError(error: Int) { stopListening(); Toast.makeText(requireContext(), "Voice error", Toast.LENGTH_SHORT).show() }
            override fun onBeginningOfSpeech() {}; override fun onRmsChanged(rmsdB: Float) {}; override fun onBufferReceived(buffer: ByteArray?) {}; override fun onEndOfSpeech() {}; override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
        })
        sr?.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()); putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        })
    }

    private fun stopListening() { sr?.stopListening(); sr?.destroy(); sr = null; isListening = false; b.btnVoiceInput.clearColorFilter() }

    override fun onDestroyView() { stopListening(); super.onDestroyView(); _b = null }
}
