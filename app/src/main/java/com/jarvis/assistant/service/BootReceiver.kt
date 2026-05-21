package com.jarvis.assistant.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.jarvis.assistant.JarvisApplication
import com.jarvis.assistant.data.repository.SettingsRepository
import dagger.hilt.android.EntryPointAccessors

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val entryPoint = EntryPointAccessors.fromApplication(context.applicationContext, BootReceiverEntryPoint::class.java)
            val repo = entryPoint.settingsRepository()
            if (repo.isOverlayEnabled()) {
                val i = Intent(context, OverlayService::class.java).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(i) else context.startService(i)
            }
            if (repo.isWakeWordEnabled()) {
                val i = Intent(context, WakeWordService::class.java).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(i) else context.startService(i)
            }
        }
    }

    interface BootReceiverEntryPoint { fun settingsRepository(): SettingsRepository }
}
