package com.jarvis.assistant.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.assistant.R
import com.jarvis.assistant.data.repository.SettingsRepository
import com.jarvis.assistant.databinding.ActivityVoiceSettingsBinding
import com.jarvis.assistant.service.WakeWordService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VoiceSettingsActivity : AppCompatActivity() {
    private lateinit var b: ActivityVoiceSettingsBinding
    @Inject lateinit var repo: SettingsRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); b = ActivityVoiceSettingsBinding.inflate(layoutInflater); setContentView(b.root)
        setSupportActionBar(b.toolbar); supportActionBar?.setDisplayHomeAsUpEnabled(true); b.toolbar.setNavigationOnClickListener { finish() }
        b.voiceOutputSwitch.isChecked = repo.isVoiceEnabled()
        b.continuousListeningSwitch.isChecked = repo.isContinuousListening()
        b.announceCallsSwitch.isChecked = repo.isAnnounceCalls()
        b.autoAnswerSwitch.isChecked = repo.isAutoAnswer()
        b.btnSave.setOnClickListener {
            repo.setVoiceEnabled(b.voiceOutputSwitch.isChecked); repo.setContinuousListening(b.continuousListeningSwitch.isChecked)
            repo.setAnnounceCalls(b.announceCallsSwitch.isChecked); repo.setAutoAnswer(b.autoAnswerSwitch.isChecked)
            if (b.continuousListeningSwitch.isChecked) { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(Intent(this, WakeWordService::class.java)) else startService(Intent(this, WakeWordService::class.java)) }
            else stopService(Intent(this, WakeWordService::class.java))
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show(); finish()
        }
    }
}
