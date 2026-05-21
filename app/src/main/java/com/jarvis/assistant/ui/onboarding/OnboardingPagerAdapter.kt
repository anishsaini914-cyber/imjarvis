package com.jarvis.assistant.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingPagerAdapter(
    private val pages: List<OnboardingPage>,
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = pages.size
    override fun createFragment(position: Int): Fragment = OnboardingFragment.newInstance(pages[position])
}
