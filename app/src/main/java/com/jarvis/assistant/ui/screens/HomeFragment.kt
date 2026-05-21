package com.jarvis.assistant.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jarvis.assistant.R
import com.jarvis.assistant.data.repository.ChatRepository
import com.jarvis.assistant.data.repository.WeatherRepository
import com.jarvis.assistant.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _b: FragmentHomeBinding? = null; private val b get() = _b!!
    @Inject lateinit var chatRepository: ChatRepository
    @Inject lateinit var weatherRepository: WeatherRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentHomeBinding.inflate(inflater, container, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.cardWhatsapp.setOnClickListener { launchApp("com.whatsapp") }
        b.cardCamera.setOnClickListener { launchApp("com.google.android.GoogleCamera") }
        b.cardFlashlight.setOnClickListener { toggleFlashlight() }
        b.cardBattery.setOnClickListener { startActivity(Intent(Intent.ACTION_POWER_USAGE_SUMMARY)) }
        b.cardSearch.setOnClickListener { (activity as? com.jarvis.assistant.MainActivity)?.navigateToFragment(ChatFragment()) }
        loadWeather()
        chatRepository.getAllChats().observe(viewLifecycleOwner) { chats ->
            if (chats.isEmpty()) { b.recentChatsLabel.visibility = View.GONE; b.emptyState.visibility = View.VISIBLE }
            else { b.recentChatsLabel.visibility = View.VISIBLE; b.emptyState.visibility = View.GONE }
        }
    }

    private fun launchApp(pkg: String) {
        try { startActivity(requireContext().packageManager.getLaunchIntentForPackage(pkg)?.also { startActivity(it) }) }
        catch (_: Exception) { Toast.makeText(requireContext(), "Could not open", Toast.LENGTH_SHORT).show() }
    }

    private fun toggleFlashlight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            try { val id = requireContext().getSystemService(android.hardware.camera2.CameraManager::class.java).cameraIdList[0]
                requireContext().getSystemService(android.hardware.camera2.CameraManager::class.java).setTorchMode(id, !b.cardFlashlight.isSelected)
                b.cardFlashlight.isSelected = !b.cardFlashlight.isSelected
            } catch (_: Exception) { Toast.makeText(requireContext(), "Flashlight error", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun loadWeather() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lifecycleScope.launch { weatherRepository.getWeather().onSuccess { w ->
                b.weatherCard.visibility = View.VISIBLE; b.weatherEmoji.text = w.emoji; b.weatherTemp.text = "${w.temperature.toInt()}\u00B0"; b.weatherDesc.text = w.description
            }.onFailure { b.weatherCard.visibility = View.GONE } }
        } else b.weatherCard.visibility = View.GONE
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
