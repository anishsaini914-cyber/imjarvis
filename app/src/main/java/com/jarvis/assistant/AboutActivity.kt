package com.jarvis.assistant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.assistant.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.versionText.text = "Version ${getString(R.string.version_name)}"
        binding.developerName.text = getString(R.string.developer_name)
        binding.developerEmail.text = getString(R.string.developer_email)
    }
}
