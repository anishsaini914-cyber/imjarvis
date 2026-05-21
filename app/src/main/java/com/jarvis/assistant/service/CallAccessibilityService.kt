package com.jarvis.assistant.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class CallAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val pkg = event.packageName?.toString() ?: ""
            if (pkg.contains("incallui") || pkg.contains("dialer") || pkg.contains("telecom")) {
                rootInActiveWindow?.findAccessibilityNodeInfosByViewId("com.android.incallui:id/acceptButton")
                    ?.firstOrNull()?.let { sendBroadcast(Intent(ACTION_CALL_RINGING)) }
            }
        }
    }
    override fun onInterrupt() {}

    companion object {
        const val ACTION_CALL_RINGING = "com.jarvis.assistant.action.CALL_RINGING"
        const val ACTION_CALL_ANSWERED = "com.jarvis.assistant.action.CALL_ANSWERED"
        const val ACTION_CALL_ENDED = "com.jarvis.assistant.action.CALL_ENDED"
    }
}
