package com.jarvis.assistant.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.jarvis.assistant.R
import com.jarvis.assistant.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackgroundListenerService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(Constants.NOTIFICATION_ID_VOICE, NotificationCompat.Builder(this, Constants.CHANNEL_VOICE)
            .setContentTitle("Jarvis Listening").setContentText("Voice assistant is active")
            .setSmallIcon(android.R.drawable.ic_media_play).setOngoing(true).build())
        return START_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? = null
}
