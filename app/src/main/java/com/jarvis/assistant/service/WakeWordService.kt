package com.jarvis.assistant.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.core.app.NotificationCompat
import com.jarvis.assistant.JarvisApplication
import com.jarvis.assistant.R
import com.jarvis.assistant.data.repository.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class WakeWordService : Service() {
    @Inject lateinit var settingsRepository: SettingsRepository
    private var sr: SpeechRecognizer? = null
    private var listening = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIF_ID, NotificationCompat.Builder(this, JarvisApplication.CHANNEL_VOICE)
            .setContentTitle("Jarvis").setContentText("Listening for wake word").setSmallIcon(R.drawable.ic_jarvis).setOngoing(true).build())
        startListening(); return START_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? = null

    private fun startListening() {
        if (listening) return; listening = true
        sr = SpeechRecognizer.createSpeechRecognizer(this)
        sr?.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?.let { text ->
                    if (text.lowercase().contains(settingsRepository.getWakeWord().lowercase())) onWakeWordDetected()
                }
                restart()
            }
            override fun onPartialResults(partialResults: Bundle?) {
                partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?.let { text ->
                    if (text.lowercase().contains(settingsRepository.getWakeWord().lowercase())) onWakeWordDetected()
                }
            }
            override fun onError(error: Int) { restart() }
            override fun onReadyForSpeech(params: Bundle?) { listening = true }
            override fun onBeginningOfSpeech() {}; override fun onRmsChanged(rmsdB: Float) {}; override fun onBufferReceived(buffer: ByteArray?) {}; override fun onEndOfSpeech() {}; override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        sr?.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        })
    }

    private fun restart() { stop(); startListening() }
    private fun stop() { sr?.stopListening(); sr?.destroy(); sr = null; listening = false }

    private fun onWakeWordDetected() {
        packageManager.getLaunchIntentForPackage(packageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP); putExtra("open_chat", true)
        }?.let { startActivity(it) }
    }

    override fun onDestroy() { stop(); super.onDestroy() }
    companion object { private const val NOTIF_ID = 1001 }
}
