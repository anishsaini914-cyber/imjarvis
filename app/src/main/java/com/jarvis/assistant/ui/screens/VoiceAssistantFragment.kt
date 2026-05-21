package com.jarvis.assistant.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jarvis.assistant.R
import com.jarvis.assistant.data.repository.ChatRepository
import com.jarvis.assistant.databinding.FragmentVoiceAssistantBinding
import com.jarvis.assistant.util.Constants
import com.jarvis.assistant.voice.SpeechRecognizerHelper
import com.jarvis.assistant.voice.SpeechResult
import com.jarvis.assistant.voice.TextToSpeechHelper
import com.jarvis.assistant.voice.TtsInitResult
import com.jarvis.assistant.voice.VoiceCommand
import com.jarvis.assistant.voice.VoiceCommandParser
import com.jarvis.assistant.weather.WeatherManager
import com.jarvis.assistant.service.OverlayService
import com.jarvis.assistant.util.AppUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.jarvis.assistant.data.local.preferences.EncryptedPreferences

@AndroidEntryPoint
class VoiceAssistantFragment : Fragment() {

    private var _binding: FragmentVoiceAssistantBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var speechRecognizerHelper: SpeechRecognizerHelper

    @Inject
    lateinit var textToSpeechHelper: TextToSpeechHelper

    @Inject
    lateinit var commandParser: VoiceCommandParser

    @Inject
    lateinit var weatherManager: WeatherManager

    @Inject
    lateinit var chatRepository: ChatRepository

    @Inject
    lateinit var encryptedPreferences: EncryptedPreferences

    private var isListening = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVoiceAssistantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTts()

        binding.micCard.setOnClickListener {
            if (isListening) {
                stopListening()
            } else {
                startListening()
            }
        }

        binding.btnHelp.setOnClickListener {
            showHelp()
        }
    }

    private fun initTts() {
        lifecycleScope.launch {
            textToSpeechHelper.initialize().collect { result ->
                when (result) {
                    is TtsInitResult.Success -> {}
                    is TtsInitResult.Error -> {}
                }
            }
        }
    }

    private fun startListening() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.tvStatus.text = "Microphone permission required"
            return
        }

        isListening = true
        binding.tvStatus.text = "Listening..."
        binding.tvTranscription.text = ""
        binding.tvResponse.text = ""
        binding.ivMicIcon.setImageResource(R.drawable.ic_stop)

        lifecycleScope.launch {
            speechRecognizerHelper.startListening().collect { result ->
                when (result) {
                    is SpeechResult.Ready -> {
                        binding.tvStatus.text = "Speak now..."
                    }
                    is SpeechResult.Starting -> {
                        binding.tvStatus.text = "Listening..."
                    }
                    is SpeechResult.PartialResult -> {
                        binding.tvTranscription.text = result.text
                    }
                    is SpeechResult.Result -> {
                        binding.tvTranscription.text = result.text
                        binding.tvStatus.text = "Processing..."
                        processVoiceCommand(result.text)
                        isListening = false
                        binding.ivMicIcon.setImageResource(R.drawable.ic_mic)
                    }
                    is SpeechResult.RmsChanged -> {}
                    is SpeechResult.EndOfSpeech -> {
                        binding.tvStatus.text = "Processing..."
                    }
                    is SpeechResult.Error -> {
                        binding.tvStatus.text = "Error: ${result.message}"
                        isListening = false
                        binding.ivMicIcon.setImageResource(R.drawable.ic_mic)
                    }
                }
            }
        }
    }

    private fun stopListening() {
        speechRecognizerHelper.stopListening()
        isListening = false
        binding.tvStatus.text = "Tap to start listening"
        binding.ivMicIcon.setImageResource(R.drawable.ic_mic)
    }

    private fun processVoiceCommand(text: String) {
        val command = commandParser.parse(text)

        lifecycleScope.launch {
            val response = when (command) {
                is VoiceCommand.AcceptCall -> {
                    "Accepting the call"
                }
                is VoiceCommand.RejectCall -> {
                    "Rejecting the call"
                }
                is VoiceCommand.SpeakerOn -> {
                    "Turning on speaker"
                }
                is VoiceCommand.MuteCall -> {
                    "Muting the call"
                }
                is VoiceCommand.UnmuteCall -> {
                    "Unmuting the call"
                }
                is VoiceCommand.WhoIsCalling -> {
                    "Checking who is calling"
                }
                is VoiceCommand.OpenWhatsApp -> {
                    AppUtils.openWhatsApp(requireContext())
                    "Opening WhatsApp"
                }
                is VoiceCommand.OpenCamera -> {
                    AppUtils.openCamera(requireContext())
                    "Opening Camera"
                }
                is VoiceCommand.OpenSettings -> {
                    AppUtils.openSettings(requireContext())
                    "Opening Settings"
                }
                is VoiceCommand.FlashlightOn -> {
                    AppUtils.toggleFlashlight(requireContext(), true)
                    "Flashlight turned on"
                }
                is VoiceCommand.FlashlightOff -> {
                    AppUtils.toggleFlashlight(requireContext(), false)
                    "Flashlight turned off"
                }
                is VoiceCommand.SetAlarm -> {
                    "Setting alarm is not yet implemented"
                }
                is VoiceCommand.GetWeather -> {
                    val weatherResult = weatherManager.getWeather()
                    weatherResult.fold(
                        onSuccess = { weather ->
                            weatherManager.formatWeatherSummary(weather)
                        },
                        onFailure = {
                            "Unable to fetch weather"
                        }
                    )
                }
                is VoiceCommand.BatteryInfo -> {
                    AppUtils.getBatteryInfo(requireContext())
                }
                is VoiceCommand.ReadNotifications -> {
                    "Reading notifications"
                }
                is VoiceCommand.OpenMusic -> {
                    AppUtils.openMusic(requireContext())
                    "Opening music player"
                }
                is VoiceCommand.BrowserSearch -> {
                    AppUtils.searchBrowser(requireContext(), text)
                    "Searching for $text"
                }
                is VoiceCommand.OpenApp -> {
                    AppUtils.openApp(requireContext(), command.appName)
                    "Opening ${command.appName}"
                }
                is VoiceCommand.StopListening -> {
                    "Stopped listening"
                }
                is VoiceCommand.Help -> {
                    commandParser.getHelpText()
                }
                is VoiceCommand.GeneralQuery -> {
                    val provider = encryptedPreferences.getString(Constants.PREFS_AI_PROVIDER, "openai")
                    val model = encryptedPreferences.getString(Constants.PREFS_AI_MODEL, "")
                    chatRepository.sendMessage(text, true, provider, model)
                    val recentMessages = chatRepository.getRecentMessages(10)
                    val aiResponse = com.jarvis.assistant.data.repository.AiRepository(
                        com.jarvis.assistant.ai.OpenAiProvider(
                            com.jarvis.assistant.data.remote.api.OpenAiApi::class.java
                                .let { TODO("inject properly") }
                        ),
                        TODO(), TODO(), TODO(), encryptedPreferences
                    ).sendMessage(recentMessages.toList())
                    aiResponse.content
                }
            }

            binding.tvResponse.text = response
            speakResponse(response)
        }
    }

    private fun speakResponse(text: String) {
        lifecycleScope.launch {
            textToSpeechHelper.speak(text).collect {}
        }
    }

    private fun showHelp() {
        binding.tvResponse.text = commandParser.getHelpText()
    }

    override fun onDestroyView() {
        speechRecognizerHelper.cancel()
        super.onDestroyView()
        _binding = null
    }
}
