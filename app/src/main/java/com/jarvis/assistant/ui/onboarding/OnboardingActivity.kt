package com.jarvis.assistant.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.jarvis.assistant.R
import com.jarvis.assistant.data.storage.PreferencesManager
import com.jarvis.assistant.ui.home.HomeActivity
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

data class OnboardingPage(val imageRes: Int, val title: String, val description: String)

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    @Inject lateinit var preferencesManager: PreferencesManager
    private lateinit var viewPager: ViewPager2

    private val pages = listOf(
        OnboardingPage(R.drawable.ic_jarvis_logo, "Welcome to Jarvis", "Your intelligent AI assistant powered by multiple AI providers"),
        OnboardingPage(android.R.drawable.ic_btn_speak_now, "Voice Control", "Control your phone with voice commands"),
        OnboardingPage(android.R.drawable.ic_menu_compass, "Smart Chat", "Chat with AI using OpenAI, Gemini, or AgentRouter")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        viewPager = findViewById(R.id.viewPager)
        val btnNext = findViewById<MaterialButton>(R.id.btnNext)
        val btnSkip = findViewById<MaterialButton>(R.id.btnSkip)

        viewPager.adapter = OnboardingAdapter(pages)

        btnNext.setOnClickListener {
            if (viewPager.currentItem < pages.size - 1) viewPager.currentItem = viewPager.currentItem + 1
            else { preferencesManager.onboardingDone = true; startActivity(Intent(this, HomeActivity::class.java)); finish() }
        }
        btnSkip.setOnClickListener { preferencesManager.onboardingDone = true; startActivity(Intent(this, HomeActivity::class.java)); finish() }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) { btnNext.text = if (position == pages.size - 1) "Get Started" else "Next" }
        })
    }
}
