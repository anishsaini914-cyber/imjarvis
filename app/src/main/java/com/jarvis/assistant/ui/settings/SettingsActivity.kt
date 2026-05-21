package com.jarvis.assistant.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.assistant.R
import com.jarvis.assistant.databinding.ActivitySettingsBinding
import com.jarvis.assistant.ui.about.AboutActivity
import com.jarvis.assistant.ui.aiprovider.AIProviderSettingsActivity
import com.jarvis.assistant.ui.modelmanager.ModelManagerActivity
import com.jarvis.assistant.ui.notifications.NotificationsActivity
import com.jarvis.assistant.ui.permissions.PermissionsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnAIProviders.setOnClickListener { startActivity(Intent(this, AIProviderSettingsActivity::class.java)) }
        binding.btnLocalModels.setOnClickListener { startActivity(Intent(this, ModelManagerActivity::class.java)) }
        binding.btnWakeWord.setOnClickListener { startActivity(Intent(this, WakeWordSettingsActivity::class.java)) }
        binding.btnPermissions.setOnClickListener { startActivity(Intent(this, PermissionsActivity::class.java)) }
        binding.btnNotifications.setOnClickListener { startActivity(Intent(this, NotificationsActivity::class.java)) }
        binding.btnAbout.setOnClickListener { startActivity(Intent(this, AboutActivity::class.java)) }
        binding.btnTheme.setOnClickListener {
            val themes = arrayOf("Dark", "Light", "System")
            android.app.AlertDialog.Builder(this).setTitle("Select Theme").setItems(themes) { _, w ->
                binding.currentTheme.text = themes[w]
            }.show()
        }
    }
}
