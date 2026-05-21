package com.jarvis.assistant.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.assistant.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var b: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); b = ActivitySettingsBinding.inflate(layoutInflater); setContentView(b.root)
        setSupportActionBar(b.toolbar); supportActionBar?.setDisplayHomeAsUpEnabled(true); b.toolbar.setNavigationOnClickListener { finish() }
    }
}
