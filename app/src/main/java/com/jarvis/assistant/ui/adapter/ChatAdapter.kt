package com.jarvis.assistant.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.assistant.R
import com.jarvis.assistant.databinding.ItemChatMessageBinding
import com.jarvis.assistant.domain.model.ChatMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter : ListAdapter<ChatMessage, ChatAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(private val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(msg: ChatMessage) {
            binding.messageText.text = msg.content
            val params = binding.messageBubble.layoutParams as ViewGroup.MarginLayoutParams
            if (msg.isUser) {
                params.marginStart = 64; params.marginEnd = 0
                binding.messageBubble.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.bubble_user))
                binding.messageBubble.strokeWidth = 0
            } else {
                params.marginStart = 0; params.marginEnd = 64
                binding.messageBubble.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.bubble_ai))
                binding.messageBubble.strokeColor = ContextCompat.getColor(binding.root.context, R.color.white_alpha_10)
                binding.messageBubble.strokeWidth = 1
            }
            binding.messageBubble.layoutParams = params
            binding.messageTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(msg.timestamp))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(o: ChatMessage, n: ChatMessage) = o.id == n.id
        override fun areContentsTheSame(o: ChatMessage, n: ChatMessage) = o == n
    }
}
