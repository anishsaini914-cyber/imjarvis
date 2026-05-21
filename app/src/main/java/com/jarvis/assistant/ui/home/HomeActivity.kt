package com.jarvis.assistant.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.assistant.R
import com.jarvis.assistant.ui.about.AboutActivity
import com.jarvis.assistant.ui.aiprovider.AiProviderActivity
import com.jarvis.assistant.ui.chat.ChatActivity
import com.jarvis.assistant.ui.modelmanager.ModelManagerActivity
import com.jarvis.assistant.ui.notifications.NotificationsActivity
import com.jarvis.assistant.ui.permissions.PermissionsActivity
import com.jarvis.assistant.ui.settings.SettingsActivity
import com.jarvis.assistant.ui.voice.VoiceActivity
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        findViewById<MaterialCardView>(R.id.cardChat).setOnClickListener { startActivity(Intent(this, ChatActivity::class.java)) }
        findViewById<MaterialCardView>(R.id.cardVoice).setOnClickListener { startActivity(Intent(this, VoiceActivity::class.java)) }
        findViewById<MaterialCardView>(R.id.cardSettings).setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
        findViewById<MaterialCardView>(R.id.cardAiProvider).setOnClickListener { startActivity(Intent(this, AiProviderActivity::class.java)) }
        findViewById<MaterialCardView>(R.id.cardModels).setOnClickListener { startActivity(Intent(this, ModelManagerActivity::class.java)) }
        findViewById<MaterialCardView>(R.id.cardNotifications).setOnClickListener { startActivity(Intent(this, NotificationsActivity::class.java)) }
        findViewById<MaterialCardView>(R.id.cardPermissions).setOnClickListener { startActivity(Intent(this, PermissionsActivity::class.java)) }
        findViewById<MaterialCardView>(R.id.cardAbout).setOnClickListener { startActivity(Intent(this, AboutActivity::class.java)) }
    }
}
