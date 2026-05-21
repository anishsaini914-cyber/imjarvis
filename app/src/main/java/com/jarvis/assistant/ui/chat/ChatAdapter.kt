package com.jarvis.assistant.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.assistant.R
import com.jarvis.assistant.domain.model.Message

class ChatAdapter(private var messages: List<Message>) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {
    fun updateMessages(newMessages: List<Message>) { messages = newMessages; notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder =
        MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false))
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) { holder.bind(messages[position]) }
    override fun getItemCount() = messages.size

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentText: TextView = itemView.findViewById(R.id.messageContent)
        private val roleIndicator: TextView = itemView.findViewById(R.id.roleIndicator)
        fun bind(message: Message) { contentText.text = message.content; roleIndicator.text = if (message.role == "user") "You" else "Jarvis" }
    }
}
