package com.jarvis.assistant.ui.chat

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jarvis.assistant.R
import com.jarvis.assistant.databinding.ActivityChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ChatAdapter(emptyList())
        binding.recyclerView.apply { layoutManager = LinearLayoutManager(this@ChatActivity).also { it.stackFromEnd = true }; adapter = this@ChatActivity.adapter }

        binding.btnSend.setOnClickListener { val text = binding.etMessage.text.toString().trim(); if (text.isNotEmpty()) { viewModel.sendMessage(text); binding.etMessage.text?.clear() } }
        binding.btnVoiceInput.setOnClickListener { /* voice input */ }

        viewModel.messages.observe(this) { messages -> adapter.updateMessages(messages); binding.recyclerView.scrollToPosition(messages.size - 1) }
        viewModel.isLoading.observe(this) { binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE }
        viewModel.error.observe(this) { it?.let { com.jarvis.assistant.util.Extensions.showToast(this@ChatActivity, it) } }
        viewModel.loadMessages()
    }
}
