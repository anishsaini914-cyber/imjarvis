package com.jarvis.assistant.ui.screens

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.assistant.R
import com.jarvis.assistant.data.repository.SettingsRepository
import com.jarvis.assistant.databinding.ActivityWakeWordSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WakeWordSettingsActivity : AppCompatActivity() {
    private lateinit var b: ActivityWakeWordSettingsBinding
    @Inject lateinit var repo: SettingsRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); b = ActivityWakeWordSettingsBinding.inflate(layoutInflater); setContentView(b.root)
        setSupportActionBar(b.toolbar); supportActionBar?.setDisplayHomeAsUpEnabled(true); b.toolbar.setNavigationOnClickListener { finish() }
        b.wakeWordInput.setText(repo.getWakeWord()); b.wakeWordSwitch.isChecked = repo.isWakeWordEnabled()
        b.btnSave.setOnClickListener {
            if (b.wakeWordInput.text.isBlank()) { Toast.makeText(this, "Enter wake word", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            repo.setWakeWord(b.wakeWordInput.text.toString()); repo.setWakeWordEnabled(b.wakeWordSwitch.isChecked)
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show(); finish()
        }
    }
}
