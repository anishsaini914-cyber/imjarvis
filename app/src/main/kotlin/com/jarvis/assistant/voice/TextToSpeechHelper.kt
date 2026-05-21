package com.jarvis.assistant.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextToSpeechHelper @Inject constructor(private val context: Context) {
    private var tts: TextToSpeech? = null
    private val _state = Channel<TtsState>(Channel.BUFFERED)
    val state: Flow<TtsState> = _state.receiveAsFlow()

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.forLanguageTag("en-IN")
                _state.trySend(TtsState.Initialized)
            }
        }
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(id: String?) { _state.trySend(TtsState.Speaking) }
            override fun onDone(id: String?) { _state.trySend(TtsState.Finished) }
            override fun onError(id: String?) { _state.trySend(TtsState.Error("Playback error")) }
        })
    }

    fun speak(text: String) { tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utterance") }
    fun stop() { tts?.stop() }
    fun shutdown() { tts?.stop(); tts?.shutdown(); tts = null }
}

sealed class TtsState {
    data object Initialized : TtsState()
    data object Speaking : TtsState()
    data object Finished : TtsState()
    data class Error(val msg: String) : TtsState()
}
