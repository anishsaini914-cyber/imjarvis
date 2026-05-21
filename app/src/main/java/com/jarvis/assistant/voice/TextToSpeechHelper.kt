package com.jarvis.assistant.voice

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextToSpeechHelper @Inject constructor(@ApplicationContext private val context: Context) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    fun init() {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) { tts?.language = Locale.ENGLISH; isInitialized = true }
        }
    }

    fun speak(text: String, callback: (() -> Unit)? = null) {
        if (!isInitialized) { init(); return }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utterance_id")
        else @Suppress("DEPRECATION") tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) { callback?.invoke() }
            override fun onError(utteranceId: String?) {}
        })
    }

    fun stop() { tts?.stop() }
    fun shutdown() { tts?.stop(); tts?.shutdown(); tts = null; isInitialized = false }
    fun isSpeaking(): Boolean = tts?.isSpeaking ?: false
}
