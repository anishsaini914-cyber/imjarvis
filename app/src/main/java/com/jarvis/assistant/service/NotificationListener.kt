package com.jarvis.assistant.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {

    private val history = mutableListOf<String>()

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val text = sbn.notification.extras.getCharSequence(android.app.Notification.EXTRA_TEXT)?.toString() ?: ""
        val title = sbn.notification.extras.getCharSequence(android.app.Notification.EXTRA_TITLE)?.toString() ?: ""
        history.add("[${sbn.packageName}] $title: $text")
        if (history.size > 100) history.removeAt(0)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {}

    fun getLatest(count: Int = 5): List<String> = history.takeLast(count)
}
