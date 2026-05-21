package com.jarvis.assistant.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.assistant.R

class QuickActionAdapter(
    private val actions: List<QuickAction>,
    private val onItemClick: (QuickAction) -> Unit
) : RecyclerView.Adapter<QuickActionAdapter.ViewHolder>() {

    data class QuickAction(
        val title: String,
        val iconRes: Int,
        val description: String
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quick_action, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val action = actions[position]
        holder.icon.setImageResource(action.iconRes)
        holder.title.text = action.title
        holder.description.text = action.description
        holder.itemView.setOnClickListener { onItemClick(action) }
    }

    override fun getItemCount(): Int = actions.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.ivActionIcon)
        val title: TextView = view.findViewById(R.id.tvActionTitle)
        val description: TextView = view.findViewById(R.id.tvActionDescription)
    }
}
