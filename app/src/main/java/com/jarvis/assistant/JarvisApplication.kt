package com.jarvis.assistant

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JarvisApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(NotificationChannel(
                CHANNEL_VOICE, "Voice Services", NotificationManager.IMPORTANCE_LOW
            ).apply { setSound(null, null) })
            nm.createNotificationChannel(NotificationChannel(
                CHANNEL_OVERLAY, "Overlay Service", NotificationManager.IMPORTANCE_LOW
            ).apply { setSound(null, null) })
            nm.createNotificationChannel(NotificationChannel(
                CHANNEL_CALL, "Call Services", NotificationManager.IMPORTANCE_HIGH
            ))
        }
    }

    companion object {
        const val CHANNEL_VOICE = "jarvis_voice"
        const val CHANNEL_OVERLAY = "jarvis_overlay"
        const val CHANNEL_CALL = "jarvis_call"
    }
}
