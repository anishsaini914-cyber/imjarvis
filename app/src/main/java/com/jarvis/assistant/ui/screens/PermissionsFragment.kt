package com.jarvis.assistant.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.jarvis.assistant.R
import com.jarvis.assistant.databinding.FragmentPermissionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionsFragment : Fragment() {
    private var _b: FragmentPermissionsBinding? = null; private val b get() = _b!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentPermissionsBinding.inflate(inflater, container, false); return b.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.permissionMicrophone.setOnClickListener { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 100) }
        b.permissionOverlay.setOnClickListener { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(requireContext())) startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply { data = Uri.parse("package:${requireContext().packageName}") }) }
        b.permissionNotification.setOnClickListener { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100) }
        b.permissionPhone.setOnClickListener { requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), 100) }
        b.permissionLocation.setOnClickListener { requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100) }
        updateStatus()
    }
    override fun onResume() { super.onResume(); updateStatus() }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) { super.onRequestPermissionsResult(requestCode, permissions, grantResults); updateStatus() }

    private fun updateStatus() {
        fun status(perm: String) = if (ContextCompat.checkSelfPermission(requireContext(), perm) == PackageManager.PERMISSION_GRANTED) R.drawable.ic_check else R.drawable.ic_close
        b.permissionMicrophone.setCompoundDrawablesWithIntrinsicBounds(0, 0, status(Manifest.permission.RECORD_AUDIO), 0)
        b.permissionPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, status(Manifest.permission.READ_PHONE_STATE), 0)
        b.permissionLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0, status(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) b.permissionNotification.setCompoundDrawablesWithIntrinsicBounds(0, 0, status(Manifest.permission.POST_NOTIFICATIONS), 0)
        val overlay = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Settings.canDrawOverlays(requireContext()) else true
        b.permissionOverlay.setCompoundDrawablesWithIntrinsicBounds(0, 0, if (overlay) R.drawable.ic_check else R.drawable.ic_close, 0)
    }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
