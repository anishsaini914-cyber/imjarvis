package com.jarvis.assistant

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.jarvis.assistant.data.repository.SettingsRepository
import com.jarvis.assistant.ui.adapter.OnboardingAdapter
import com.jarvis.assistant.databinding.ActivityOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    @Inject lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = OnboardingAdapter()
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val isLast = position == adapter.itemCount - 1
                binding.btnNext.text = if (isLast) getString(R.string.finish) else getString(R.string.next)
                binding.btnSkip.visibility = if (isLast) android.view.View.GONE else android.view.View.VISIBLE
                binding.dotsIndicator.setSelectedDotColor(position)
            }
        })
        binding.btnNext.setOnClickListener {
            if (binding.viewPager.currentItem < adapter.itemCount - 1)
                binding.viewPager.currentItem = binding.viewPager.currentItem + 1
            else finishOnboarding()
        }
        binding.btnSkip.setOnClickListener { finishOnboarding() }
    }

    private fun finishOnboarding() {
        settingsRepository.setOnboardingComplete(true)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
