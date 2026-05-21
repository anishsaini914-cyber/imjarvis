package com.jarvis.assistant.ui.settings

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jarvis.assistant.data.storage.SettingsStorage
import com.jarvis.assistant.databinding.ActivityWakeWordSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WakeWordSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWakeWordSettingsBinding

    @Inject lateinit var settingsStorage: SettingsStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityWakeWordSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        lifecycleScope.launch {
            binding.wakeWordSwitch.isChecked = settingsStorage.isWakeWordEnabled().first()
            binding.sensitivitySeekBar.progress = settingsStorage.getWakeWordSensitivity().first()
            binding.sensitivityValue.text = "${binding.sensitivitySeekBar.progress}%"
        }

        binding.wakeWordSwitch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch { settingsStorage.setWakeWordEnabled(isChecked) }
        }

        binding.sensitivitySeekBar.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: android.widget.SeekBar, p: Int, f: Boolean) { binding.sensitivityValue.text = "$p%" }
            override fun onStartTrackingTouch(s: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(s: android.widget.SeekBar?) {
                lifecycleScope.launch { settingsStorage.setWakeWordSensitivity(binding.sensitivitySeekBar.progress) }
            }
        })
    }
}
