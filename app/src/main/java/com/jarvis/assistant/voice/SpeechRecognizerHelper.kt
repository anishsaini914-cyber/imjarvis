package com.jarvis.assistant.voice

import android.content.Context
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeechRecognizerHelper @Inject constructor(@ApplicationContext private val context: Context) {
    private var speechRecognizer: SpeechRecognizer? = null

    fun startListening(listener: SpeechListener) {
        speechRecognizer?.destroy()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() { listener.onSpeechStart() }
            override fun onRmsChanged(rmsdB: Float) { listener.onRmsChanged(rmsdB) }
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { listener.onSpeechEnd() }
            override fun onError(error: Int) { listener.onError(error) }
            override fun onResults(results: Bundle?) { listener.onResult(results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull() ?: "") }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        speechRecognizer?.startListening(RecognizerIntent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        })
    }

    fun stopListening() { speechRecognizer?.stopListening(); speechRecognizer?.destroy(); speechRecognizer = null }
    fun isListening(): Boolean = speechRecognizer != null

    interface SpeechListener {
        fun onSpeechStart(); fun onSpeechEnd(); fun onResult(text: String); fun onError(error: Int); fun onRmsChanged(rmsdB: Float)
    }
}
