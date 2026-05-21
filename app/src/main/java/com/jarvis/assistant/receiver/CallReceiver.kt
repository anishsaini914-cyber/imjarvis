package com.jarvis.assistant.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.jarvis.assistant.service.CallHandlerService

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            context.startService(Intent(context, CallHandlerService::class.java).putExtras(intent.extras ?: return))
        }
    }
}
