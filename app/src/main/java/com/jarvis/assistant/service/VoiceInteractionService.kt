package com.jarvis.assistant.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import com.jarvis.assistant.data.repository.AiRepository
import com.jarvis.assistant.data.repository.SettingsRepository
import com.jarvis.assistant.domain.model.Message
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class VoiceInteractionService : Service() {
    @Inject lateinit var settingsRepository: SettingsRepository
    @Inject lateinit var aiRepository: AiRepository
    private var sr: SpeechRecognizer? = null
    private var tts: TextToSpeech? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this) { if (it == TextToSpeech.SUCCESS) tts?.language = Locale.getDefault() }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_LISTENING -> startListening()
            ACTION_SPEAK -> intent.getStringExtra(EXTRA_TEXT)?.let { speak(it) }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    fun startListening() {
        sr = SpeechRecognizer.createSpeechRecognizer(this)
        sr?.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?.let { query ->
                    sendBroadcast(Intent(ACTION_VOICE_RESULT).putExtra(EXTRA_TEXT, query))
                    processQuery(query)
                }
            }
            override fun onError(error: Int) { sendBroadcast(Intent(ACTION_VOICE_RESULT).putExtra(EXTRA_TEXT, "")) }
            override fun onReadyForSpeech(params: Bundle?) {}; override fun onBeginningOfSpeech() {}; override fun onRmsChanged(rmsdB: Float) {}; override fun onBufferReceived(buffer: ByteArray?) {}; override fun onEndOfSpeech() {}; override fun onPartialResults(partialResults: Bundle?) {}; override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        sr?.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        })
    }

    private fun processQuery(query: String) {
        scope.launch {
            val provider = settingsRepository.getActiveProvider()
            val model = when (provider) { "openai" -> settingsRepository.getOpenAiModel(); "gemini" -> settingsRepository.getGeminiModel(); else -> "" }
            val result = withContext(Dispatchers.IO) { aiRepository.getChatCompletion(listOf(Message(chatId = 0, role = "user", content = query)), provider, model) }
            result.onSuccess { r -> if (settingsRepository.isVoiceEnabled()) speak(r); sendBroadcast(Intent(ACTION_VOICE_RESPONSE).putExtra(EXTRA_TEXT, r)) }
                .onFailure { speak("Error: ${it.message}") }
        }
    }

    private fun speak(text: String) { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null) else tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null) }

    override fun onDestroy() { sr?.destroy(); tts?.stop(); tts?.shutdown(); super.onDestroy() }

    companion object {
        const val ACTION_START_LISTENING = "com.jarvis.assistant.action.START_LISTENING"
        const val ACTION_SPEAK = "com.jarvis.assistant.action.SPEAK"
        const val ACTION_VOICE_RESULT = "com.jarvis.assistant.action.VOICE_RESULT"
        const val ACTION_VOICE_RESPONSE = "com.jarvis.assistant.action.VOICE_RESPONSE"
        const val EXTRA_TEXT = "extra_text"
    }
}
