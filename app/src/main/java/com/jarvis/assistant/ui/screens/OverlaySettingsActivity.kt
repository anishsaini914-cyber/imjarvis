package com.jarvis.assistant.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.assistant.R
import com.jarvis.assistant.data.repository.SettingsRepository
import com.jarvis.assistant.databinding.ActivityOverlaySettingsBinding
import com.jarvis.assistant.service.OverlayService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OverlaySettingsActivity : AppCompatActivity() {
    private lateinit var b: ActivityOverlaySettingsBinding
    @Inject lateinit var repo: SettingsRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); b = ActivityOverlaySettingsBinding.inflate(layoutInflater); setContentView(b.root)
        setSupportActionBar(b.toolbar); supportActionBar?.setDisplayHomeAsUpEnabled(true); b.toolbar.setNavigationOnClickListener { finish() }
        b.overlaySwitch.isChecked = repo.isOverlayEnabled()
        b.btnGrantOverlayPermission.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))
                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply { data = Uri.parse("package:$packageName") })
            else Toast.makeText(this, "Already granted", Toast.LENGTH_SHORT).show()
        }
        b.btnSave.setOnClickListener {
            repo.setOverlayEnabled(b.overlaySwitch.isChecked)
            if (b.overlaySwitch.isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))
                { Toast.makeText(this, "Grant overlay permission first", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(Intent(this, OverlayService::class.java)) else startService(Intent(this, OverlayService::class.java))
            } else stopService(Intent(this, OverlayService::class.java))
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show(); finish()
        }
    }
}
