package com.jarvis.assistant.ui.screens

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.assistant.R
import com.jarvis.assistant.data.repository.SettingsRepository
import com.jarvis.assistant.databinding.ActivityAiProviderSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AiProviderSettingsActivity : AppCompatActivity() {
    private lateinit var b: ActivityAiProviderSettingsBinding
    @Inject lateinit var repo: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); b = ActivityAiProviderSettingsBinding.inflate(layoutInflater); setContentView(b.root)
        setSupportActionBar(b.toolbar); supportActionBar?.setDisplayHomeAsUpEnabled(true); b.toolbar.setNavigationOnClickListener { finish() }

        val providers = arrayOf("OpenAI", "Gemini", "AgentRouter")
        b.providerSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, providers)
        val pos = when (repo.getActiveProvider()) { "openai" -> 0; "gemini" -> 1 else -> 0 }
        b.providerSpinner.setSelection(pos)
        b.providerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: android.view.View?, i: Int, id: Long) {
                b.openaiFields.visibility = if (i == 0) android.view.View.VISIBLE else android.view.View.GONE
                b.geminiFields.visibility = if (i == 1) android.view.View.VISIBLE else android.view.View.GONE
                b.agentRouterFields.visibility = if (i == 2) android.view.View.VISIBLE else android.view.View.GONE
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        b.openaiKey.setText(repo.getOpenAiKey()); b.openaiModel.setText(repo.getOpenAiModel())
        b.geminiKey.setText(repo.getGeminiKey()); b.geminiModel.setText(repo.getGeminiModel())
        b.agentRouterKey.setText(repo.getAgentRouterKey()); b.agentRouterModel.setText(repo.getAgentRouterModel()); b.agentRouterEndpoint.setText(repo.getAgentRouterEndpoint())

        b.btnSave.setOnClickListener {
            when (b.providerSpinner.selectedItemPosition) {
                0 -> { repo.setActiveProvider("openai"); repo.setOpenAiKey(b.openaiKey.text.toString()); repo.setOpenAiModel(b.openaiModel.text.toString()) }
                1 -> { repo.setActiveProvider("gemini"); repo.setGeminiKey(b.geminiKey.text.toString()); repo.setGeminiModel(b.geminiModel.text.toString()) }
                2 -> { repo.setActiveProvider("agentrouter"); repo.setAgentRouterKey(b.agentRouterKey.text.toString()); repo.setAgentRouterModel(b.agentRouterModel.text.toString()); repo.setAgentRouterEndpoint(b.agentRouterEndpoint.text.toString()) }
            }
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show(); finish()
        }
    }
}
