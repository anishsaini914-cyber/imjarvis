package com.jarvis.assistant.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.assistant.databinding.ActivityAboutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.developerEmail.setOnClickListener {
            startActivity(Intent.createChooser(
                Intent(Intent.ACTION_SENDTO).apply { data = Uri.parse("mailto:anishsaini939@gmail.com") }, "Send Email"
            ))
        }
    }
}
