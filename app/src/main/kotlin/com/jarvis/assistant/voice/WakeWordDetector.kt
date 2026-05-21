package com.jarvis.assistant.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.jarvis.assistant.data.local.preferences.EncryptedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WakeWordDetector @Inject constructor(
    private val context: Context,
    private val prefs: EncryptedPreferences
) {
    private var recognizer: SpeechRecognizer? = null
    private val _detected = MutableStateFlow(false)
    val detected: StateFlow<Boolean> = _detected

    private val listener = object : RecognitionListener {
        override fun onReadyForSpeech(p: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(v: Float) {}
        override fun onBufferReceived(b: ByteArray?) {}
        override fun onEndOfSpeech() { restart() }
        override fun onError(e: Int) { if (e != SpeechRecognizer.ERROR_CLIENT) restart() }
        override fun onResults(data: Bundle?) { if (checkWakeWord(data?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull() ?: "")) _detected.value = true; restart() }
        override fun onPartialResults(data: Bundle?) { if (checkWakeWord(data?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull() ?: "")) _detected.value = true }
        override fun onEvent(t: Int, p: Bundle?) {}
    }

    fun start() { _detected.value = false; listen() }
    fun stop() { recognizer?.let { it.stopListening(); it.destroy() }; recognizer = null }
    fun reset() { _detected.value = false }

    private fun listen() {
        try {
            recognizer = SpeechRecognizer.createSpeechRecognizer(context).also { it.setRecognitionListener(listener) }
            recognizer?.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            })
        } catch (_: Exception) {}
    }

    private fun restart() { recognizer?.destroy(); if (prefs.wakeWordEnabled) listen() }
    private fun checkWakeWord(text: String) = prefs.wakeWordEnabled && text.lowercase().contains(prefs.wakeWordPhrase.lowercase())
}
