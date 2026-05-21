package com.jarvis.assistant.ui.permissions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jarvis.assistant.R
import com.jarvis.assistant.databinding.ActivityPermissionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionsBinding

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        updateStatus()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.micStatus.setOnClickListener { requestPerm(Manifest.permission.RECORD_AUDIO) }
        binding.notifStatus.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) requestPerm(Manifest.permission.POST_NOTIFICATIONS)
        }
        binding.overlayStatus.setOnClickListener {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
        }
        binding.phoneStatus.setOnClickListener { requestPerm(Manifest.permission.READ_PHONE_STATE) }
        updateStatus()
    }

    private fun requestPerm(perm: String) {
        if (ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Already granted", Toast.LENGTH_SHORT).show()
        } else permissionLauncher.launch(arrayOf(perm))
    }

    private fun updateStatus() {
        fun setStatus(id: android.widget.TextView, perm: String) {
            val granted = ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED
            id.text = if (granted) "Granted" else "Not Granted"
            id.setTextColor(ContextCompat.getColor(this, if (granted) R.color.success else R.color.warning))
        }
        setStatus(binding.micStatus, Manifest.permission.RECORD_AUDIO)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            setStatus(binding.notifStatus, Manifest.permission.POST_NOTIFICATIONS)
        binding.overlayStatus.text = if (Settings.canDrawOverlays(this)) "Granted" else "Not Granted"
        binding.overlayStatus.setTextColor(ContextCompat.getColor(this,
            if (Settings.canDrawOverlays(this)) R.color.success else R.color.warning))
        setStatus(binding.phoneStatus, Manifest.permission.READ_PHONE_STATE)
    }
}
