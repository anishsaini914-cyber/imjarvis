package com.jarvis.assistant

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class JarvisApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)

            manager.createNotificationChannel(NotificationChannel(
                CHANNEL_WAKE_WORD, "Wake Word", NotificationManager.IMPORTANCE_LOW
            ).apply { description = "Wake word detection service"; setSound(null, null) })

            manager.createNotificationChannel(NotificationChannel(
                CHANNEL_OVERLAY, "Overlay", NotificationManager.IMPORTANCE_LOW
            ).apply { description = "Floating overlay service" })

            manager.createNotificationChannel(NotificationChannel(
                CHANNEL_VOICE, "Voice Assistant", NotificationManager.IMPORTANCE_LOW
            ).apply { description = "Voice interaction service" })

            manager.createNotificationChannel(NotificationChannel(
                CHANNEL_CALL, "Call Assistant", NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Call handling service" })

            manager.createNotificationChannel(NotificationChannel(
                CHANNEL_GENERAL, "Jarvis", NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "General notifications" })
        }
    }

    companion object {
        const val CHANNEL_WAKE_WORD = "jarvis_wake_word"
        const val CHANNEL_OVERLAY = "jarvis_overlay"
        const val CHANNEL_VOICE = "jarvis_voice"
        const val CHANNEL_CALL = "jarvis_call"
        const val CHANNEL_GENERAL = "jarvis_general"
    }
}
