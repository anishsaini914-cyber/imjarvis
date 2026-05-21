package com.jarvis.assistant.ui.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.jarvis.assistant.R
import com.jarvis.assistant.databinding.FragmentHomeDashboardBinding
import com.jarvis.assistant.ui.adapters.QuickActionAdapter
import com.jarvis.assistant.util.Constants
import com.jarvis.assistant.weather.WeatherManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import com.jarvis.assistant.data.local.preferences.EncryptedPreferences

@AndroidEntryPoint
class HomeDashboardFragment : Fragment() {

    private var _binding: FragmentHomeDashboardBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var weatherManager: WeatherManager

    @Inject
    lateinit var encryptedPreferences: EncryptedPreferences

    private val quickActions = listOf(
        QuickActionAdapter.QuickAction("Chat", R.drawable.ic_chat, "Talk with AI"),
        QuickActionAdapter.QuickAction("Voice", R.drawable.ic_mic, "Voice commands"),
        QuickActionAdapter.QuickAction("Overlay", R.drawable.ic_overlay, "Floating bubble"),
        QuickActionAdapter.QuickAction("Weather", R.drawable.ic_weather, "Check weather"),
        QuickActionAdapter.QuickAction("Flashlight", R.drawable.ic_bot, "Toggle light"),
        QuickActionAdapter.QuickAction("Settings", R.drawable.ic_settings, "Configure app")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGreeting()
        setupQuickActions()
        loadWeather()
        loadStatus()
    }

    private fun setupGreeting() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }
        binding.tvGreeting.text = "$greeting,"
    }

    private fun setupQuickActions() {
        val adapter = QuickActionAdapter(quickActions) { action ->
            when (action.title) {
                "Chat" -> navigateToChat()
                "Voice" -> navigateToVoice()
                "Overlay" -> toggleOverlay()
                "Weather" -> refreshWeather()
                "Flashlight" -> toggleFlashlight()
                "Settings" -> navigateToSettings()
            }
        }
        binding.quickActionsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.quickActionsRecycler.adapter = adapter
    }

    private fun loadWeather() {
        if (!weatherManager.isWeatherEnabled()) {
            binding.tvWeatherInfo.text = "Weather tracking disabled"
            return
        }

        lifecycleScope.launch {
            val result = weatherManager.getWeather()
            result.onSuccess { weather ->
                val summary = weatherManager.formatWeatherSummary(weather)
                binding.tvWeatherInfo.text = summary
            }.onFailure {
                binding.tvWeatherInfo.text = "Unable to load weather"
            }
        }
    }

    private fun loadStatus() {
        val provider = encryptedPreferences.getString(Constants.PREFS_AI_PROVIDER, "openai")
        val model = encryptedPreferences.getString(Constants.PREFS_AI_MODEL, "gpt-3.5-turbo")
        binding.tvProviderInfo.text = "Provider: ${provider.replaceFirstChar { it.uppercase() }}"
        binding.tvModelInfo.text = "Model: $model"
    }

    private fun navigateToChat() {
        val navHost = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? androidx.navigation.fragment.NavHostFragment
        navHost?.navController?.navigate(R.id.chatFragment)
    }

    private fun navigateToVoice() {
        val navHost = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? androidx.navigation.fragment.NavHostFragment
        navHost?.navController?.navigate(R.id.voiceFragment)
    }

    private fun navigateToSettings() {
        val navHost = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? androidx.navigation.fragment.NavHostFragment
        navHost?.navController?.navigate(R.id.settingsFragment)
    }

    private fun toggleOverlay() {
        com.jarvis.assistant.service.OverlayService.start(requireContext())
    }

    private fun refreshWeather() {
        loadWeather()
    }

    private fun toggleFlashlight() {
        val intent = Intent(requireContext(), com.jarvis.assistant.voice.VoiceCommand::class.java)
        // Flashlight toggled via system
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
