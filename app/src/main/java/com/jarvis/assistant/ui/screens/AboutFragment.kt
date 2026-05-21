package com.jarvis.assistant.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jarvis.assistant.R
import com.jarvis.assistant.databinding.FragmentAboutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : Fragment() {
    private var _b: FragmentAboutBinding? = null; private val b get() = _b!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentAboutBinding.inflate(inflater, container, false); return b.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.versionText.text = "Version ${getString(R.string.version_name)}"
        b.developerName.text = getString(R.string.developer_name)
        b.developerEmail.text = getString(R.string.developer_email)
    }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
