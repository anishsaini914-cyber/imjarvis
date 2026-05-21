package com.jarvis.assistant.service

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.jarvis.assistant.JarvisApplication
import com.jarvis.assistant.R

class OverlayService : Service() {
    private lateinit var wm: WindowManager
    private lateinit var overlayView: View
    private var initialX = 0; private var initialY = 0
    private var initialTouchX = 0f; private var initialTouchY = 0f

    override fun onCreate() { wm = getSystemService(WINDOW_SERVICE) as WindowManager }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIF_ID, createNotification())
        showOverlay()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification = NotificationCompat.Builder(this, JarvisApplication.CHANNEL_OVERLAY)
        .setContentTitle("Jarvis").setContentText("Overlay active").setSmallIcon(R.drawable.ic_jarvis).setOngoing(true).build()

    private fun showOverlay() {
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null)
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.START; x = 0; y = 100 }

        overlayView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> { initialX = params.x; initialY = params.y; initialTouchX = event.rawX; initialTouchY = event.rawY; true }
                MotionEvent.ACTION_MOVE -> { params.x = initialX + (event.rawX - initialTouchX).toInt(); params.y = initialY + (event.rawY - initialTouchY).toInt(); wm.updateViewLayout(overlayView, params); true }
                MotionEvent.ACTION_UP -> { if (Math.abs(event.rawX - initialTouchX) < 10 && Math.abs(event.rawY - initialTouchY) < 10) openAssistant(); true }
                else -> false
            }
        }
        try { wm.addView(overlayView, params) } catch (e: Exception) { e.printStackTrace() }
    }

    private fun openAssistant() {
        packageManager.getLaunchIntentForPackage(packageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra("open_chat", true)
        }?.let { startActivity(it) }
    }

    override fun onDestroy() { if (::overlayView.isInitialized) try { wm.removeView(overlayView) } catch (_: Exception) {}; super.onDestroy() }

    companion object { private const val NOTIF_ID = 1002 }
}
