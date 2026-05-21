package com.jarvis.assistant.ui.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jarvis.assistant.AboutActivity
import com.jarvis.assistant.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _b: FragmentSettingsBinding? = null; private val b get() = _b!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentSettingsBinding.inflate(inflater, container, false); return b.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.settingsAiProvider.setOnClickListener { startActivity(Intent(requireContext(), AiProviderSettingsActivity::class.java)) }
        b.settingsWakeWord.setOnClickListener { startActivity(Intent(requireContext(), WakeWordSettingsActivity::class.java)) }
        b.settingsOverlay.setOnClickListener { startActivity(Intent(requireContext(), OverlaySettingsActivity::class.java)) }
        b.settingsVoice.setOnClickListener { startActivity(Intent(requireContext(), VoiceSettingsActivity::class.java)) }
        b.settingsPermissions.setOnClickListener { (activity as? com.jarvis.assistant.MainActivity)?.navigateToFragment(PermissionsFragment()) }
        b.settingsAbout.setOnClickListener { startActivity(Intent(requireContext(), AboutActivity::class.java)) }
    }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
