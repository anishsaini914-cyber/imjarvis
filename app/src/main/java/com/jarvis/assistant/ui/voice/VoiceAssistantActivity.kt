package com.jarvis.assistant.ui.voice

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.assistant.databinding.ActivityVoiceAssistantBinding
import com.jarvis.assistant.util.VoiceCommandParser
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class VoiceAssistantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVoiceAssistantBinding
    private var tts: TextToSpeech? = null
    private var isListening = false

    @Inject lateinit var voiceCommandParser: VoiceCommandParser

    private val speechLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()?.let { text ->
                binding.transcribedText.text = text
                binding.statusText.text = "Processing..."
                processVoiceInput(text)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityVoiceAssistantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tts = TextToSpeech(this) { if (it == TextToSpeech.SUCCESS) tts?.language = Locale.US }
        binding.btnClose.setOnClickListener {
            if (isListening) stopListening() else startListening()
        }
    }

    private fun startListening() {
        isListening = true
        binding.statusText.text = "Listening..."
        binding.transcribedText.text = ""
        binding.responseText.visibility = View.GONE
        speechLauncher.launch(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        })
    }

    private fun stopListening() {
        isListening = false; binding.statusText.text = "Tap to speak"
    }

    private fun processVoiceInput(text: String) {
        val command = voiceCommandParser.parse(text)
        val response = voiceCommandParser.executeCommand(command, this)
        binding.responseText.visibility = View.VISIBLE
        binding.responseText.text = response
        tts?.speak(response, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() { tts?.stop(); tts?.shutdown(); super.onDestroy() }
}
