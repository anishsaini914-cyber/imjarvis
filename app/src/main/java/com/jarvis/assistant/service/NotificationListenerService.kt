package com.jarvis.assistant.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationListenerService : NotificationListenerService() {
    private val notifications = mutableListOf<String>()

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val text = sbn.notification.extras.getString(android.app.Notification.EXTRA_TEXT) ?: return
        val title = sbn.notification.extras.getString(android.app.Notification.EXTRA_TITLE) ?: ""
        notifications.add("[$title]: $text")
        if (notifications.size > 50) notifications.removeAt(0)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {}
    fun getRecentNotifications(count: Int = 5): List<String> = notifications.takeLast(count)
}
