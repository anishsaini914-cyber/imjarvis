package com.jarvis.assistant.service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.speech.tts.TextToSpeech
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.telecom.TelecomManager
import com.jarvis.assistant.data.repository.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class CallHandlerService : NotificationListenerService() {
    @Inject lateinit var settingsRepository: SettingsRepository
    private var tts: TextToSpeech? = null

    override fun onCreate() { super.onCreate(); tts = TextToSpeech(this) { if (it == TextToSpeech.SUCCESS) tts?.language = Locale.getDefault() } }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn?.packageName in listOf("com.android.incallui", "com.google.android.dialer")) {
            val title = sbn.notification.extras.getString("android.title") ?: ""
            val text = sbn.notification.extras.getString("android.text") ?: ""
            if (title.contains("Incoming", true) || text.contains("incoming", true)) {
                val caller = if (title.isNotBlank() && title != "Incoming call") title else text.ifBlank { "Unknown" }
                if (settingsRepository.isAnnounceCalls()) {
                    val msg = "Incoming call from $caller"
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) tts?.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null)
                    else tts?.speak(msg, TextToSpeech.QUEUE_FLUSH, null)
                }
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {}
    override fun onDestroy() { tts?.stop(); tts?.shutdown(); super.onDestroy() }

    companion object {
        fun acceptCall(ctx: Context) { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) (ctx.getSystemService(Context.TELECOM_SERVICE) as TelecomManager).acceptRingingCall() }
        fun rejectCall(ctx: Context) { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) (ctx.getSystemService(Context.TELECOM_SERVICE) as TelecomManager).endCall() }
    }
}
