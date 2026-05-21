package com.jarvis.assistant.ui.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.assistant.R

class OnboardingAdapter(private val pages: List<OnboardingPage>) : RecyclerView.Adapter<OnboardingAdapter.PageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder =
        PageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.onboarding_page, parent, false))
    override fun onBindViewHolder(holder: PageViewHolder, position: Int) { holder.bind(pages[position]) }
    override fun getItemCount() = pages.size

    class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.onboardingImage)
        private val titleText: TextView = itemView.findViewById(R.id.onboardingTitle)
        private val descText: TextView = itemView.findViewById(R.id.onboardingDescription)
        fun bind(page: OnboardingPage) { imageView.setImageResource(page.imageRes); titleText.text = page.title; descText.text = page.description }
    }
}
