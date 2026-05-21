package com.jarvis.assistant.ui.aiprovider

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jarvis.assistant.R
import com.jarvis.assistant.data.storage.SettingsStorage
import com.jarvis.assistant.databinding.ActivityAiProviderSettingsBinding
import com.jarvis.assistant.domain.model.AIProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AIProviderSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAiProviderSettingsBinding

    @Inject lateinit var settingsStorage: SettingsStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityAiProviderSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.providerSpinner.adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.ai_providers))

        binding.openaiModelSpinner.adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.openai_models))
        binding.geminiModelSpinner.adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.gemini_models))

        binding.providerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                showProviderCard(AIProvider.entries[pos])
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        lifecycleScope.launch {
            val active = settingsStorage.getActiveProvider().first()
            binding.providerSpinner.setSelection(active.ordinal)
            binding.openaiApiKey.setText(settingsStorage.getOpenAIApiKey().first())
            binding.openaiEndpoint.setText(settingsStorage.getOpenAIEndpoint().first())
            binding.geminiApiKey.setText(settingsStorage.getGeminiApiKey().first())
            binding.agentrouterApiKey.setText(settingsStorage.getAgentRouterApiKey().first())
            binding.agentrouterEndpoint.setText(settingsStorage.getAgentRouterEndpoint().first())
            binding.agentrouterModel.setText(settingsStorage.getAgentRouterModel().first())
        }

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                settingsStorage.setActiveProvider(AIProvider.entries[binding.providerSpinner.selectedItemPosition])
                settingsStorage.setOpenAIApiKey(binding.openaiApiKey.text.toString())
                settingsStorage.setOpenAIEndpoint(binding.openaiEndpoint.text.toString())
                settingsStorage.setGeminiApiKey(binding.geminiApiKey.text.toString())
                settingsStorage.setAgentRouterApiKey(binding.agentrouterApiKey.text.toString())
                settingsStorage.setAgentRouterEndpoint(binding.agentrouterEndpoint.text.toString())
                settingsStorage.setAgentRouterModel(binding.agentrouterModel.text.toString())
                finish()
            }
        }
    }

    private fun showProviderCard(provider: AIProvider) {
        binding.openaiCard.visibility = if (provider == AIProvider.OPENAI) View.VISIBLE else View.GONE
        binding.geminiCard.visibility = if (provider == AIProvider.GEMINI) View.VISIBLE else View.GONE
        binding.agentrouterCard.visibility = if (provider == AIProvider.AGENTROUTER) View.VISIBLE else View.GONE
    }
}
