package com.jarvis.assistant.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jarvis.assistant.databinding.ItemOnboardingPageBinding

class OnboardingFragment : Fragment() {

    private var _binding: ItemOnboardingPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ItemOnboardingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val page = arguments?.getSerializable("page") as? OnboardingPage
        page?.let { binding.titleText.text = it.title; binding.descriptionText.text = it.description }
    }

    override fun onDestroyView() { _binding = null; super.onDestroyView() }

    companion object {
        fun newInstance(page: OnboardingPage) = OnboardingFragment().apply {
            arguments = Bundle().apply { putSerializable("page", page) }
        }
    }
}
