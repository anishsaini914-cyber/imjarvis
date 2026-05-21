package com.jarvis.assistant.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.assistant.R
import com.jarvis.assistant.data.local.entity.ChatMessage

class ChatAdapter(
    private val messages: List<ChatMessage>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_USER = 0
        private const val TYPE_BOT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) TYPE_USER else TYPE_BOT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = if (viewType == TYPE_USER) {
            R.layout.item_chat_user
        } else {
            R.layout.item_chat_bot
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return if (viewType == TYPE_USER) UserViewHolder(view) else BotViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is UserViewHolder -> holder.bind(message)
            is BotViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val content: TextView = view.findViewById(R.id.tvUserMessage)
        private val time: TextView = view.findViewById(R.id.tvUserTime)

        fun bind(message: ChatMessage) {
            content.text = message.content
            time.text = android.text.format.DateFormat.format("hh:mm a", message.timestamp)
        }
    }

    class BotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val content: TextView = view.findViewById(R.id.tvBotMessage)
        private val time: TextView = view.findViewById(R.id.tvBotTime)
        private val model: TextView = view.findViewById(R.id.tvBotModel)

        fun bind(message: ChatMessage) {
            content.text = message.content
            time.text = android.text.format.DateFormat.format("hh:mm a", message.timestamp)
            if (message.model.isNotEmpty()) {
                model.text = message.model
                model.visibility = View.VISIBLE
            } else {
                model.visibility = View.GONE
            }
        }
    }
}
