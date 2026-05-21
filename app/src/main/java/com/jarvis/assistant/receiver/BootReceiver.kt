package com.jarvis.assistant.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jarvis.assistant.data.storage.PreferencesManager
import com.jarvis.assistant.service.BackgroundListenerService
import com.jarvis.assistant.ui.overlay.FloatingOverlayService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject lateinit var preferencesManager: PreferencesManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            if (preferencesManager.wakeWordEnabled) context.startForegroundService(Intent(context, BackgroundListenerService::class.java))
            if (preferencesManager.overlayEnabled) context.startForegroundService(Intent(context, FloatingOverlayService::class.java))
        }
    }
}
