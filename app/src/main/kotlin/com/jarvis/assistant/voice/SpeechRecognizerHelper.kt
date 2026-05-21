package com.jarvis.assistant.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeechRecognizerHelper @Inject constructor(private val context: Context) {
    private var recognizer: SpeechRecognizer? = null
    private val _results = Channel<SpeechResult>(Channel.BUFFERED)
    val results: Flow<SpeechResult> = _results.receiveAsFlow()

    private val listener = object : RecognitionListener {
        override fun onReadyForSpeech(p: Bundle?) {}
        override fun onBeginningOfSpeech() { _results.trySend(SpeechResult.OnBeginningOfSpeech) }
        override fun onRmsChanged(v: Float) { _results.trySend(SpeechResult.OnRmsChanged(v)) }
        override fun onBufferReceived(b: ByteArray?) {}
        override fun onEndOfSpeech() { _results.trySend(SpeechResult.OnEndOfSpeech) }
        override fun onError(e: Int) { _results.trySend(SpeechResult.OnError("Speech error: $e")) }
        override fun onResults(data: Bundle?) { _results.trySend(SpeechResult.OnResults(data?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull() ?: "")) }
        override fun onPartialResults(data: Bundle?) { _results.trySend(SpeechResult.OnPartialResults(data?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull() ?: "")) }
        override fun onEvent(t: Int, p: Bundle?) {}
    }

    fun startListening(lang: String = "en-IN") {
        stop()
        recognizer = SpeechRecognizer.createSpeechRecognizer(context).also { it.setRecognitionListener(listener) }
        recognizer?.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        })
    }

    fun stop() { recognizer?.let { it.stopListening(); it.destroy() }; recognizer = null }
}

sealed class SpeechResult {
    data object OnBeginningOfSpeech : SpeechResult()
    data class OnRmsChanged(val v: Float) : SpeechResult()
    data object OnEndOfSpeech : SpeechResult()
    data class OnResults(val text: String) : SpeechResult()
    data class OnPartialResults(val text: String) : SpeechResult()
    data class OnError(val msg: String) : SpeechResult()
}
