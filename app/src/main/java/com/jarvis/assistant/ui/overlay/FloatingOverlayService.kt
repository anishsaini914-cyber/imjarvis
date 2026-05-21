package com.jarvis.assistant.ui.overlay

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.jarvis.assistant.R
import com.jarvis.assistant.ui.voice.VoiceActivity
import com.jarvis.assistant.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FloatingOverlayService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private var params: WindowManager.LayoutParams? = null
    private var initialX = 0; private var initialY = 0; private var initialTouchX = 0f; private var initialTouchY = 0f

    override fun onCreate() { super.onCreate(); windowManager = getSystemService(WINDOW_SERVICE) as WindowManager }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showOverlay()
        startForeground(Constants.NOTIFICATION_ID_OVERLAY, NotificationCompat.Builder(this, Constants.CHANNEL_OVERLAY)
            .setContentTitle("Jarvis Assistant").setContentText("Floating overlay active")
            .setSmallIcon(android.R.drawable.ic_menu_compass).setOngoing(true).build())
        return START_STICKY
    }

    private fun showOverlay() {
        if (::overlayView.isInitialized) return
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null)
        val size = Point().also { windowManager.defaultDisplay.getRealSize(it) }
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else @Suppress("DEPRECATION") WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.START; x = size.x / 2; y = size.y / 3 }

        overlayView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> { initialX = params!!.x; initialY = params!!.y; initialTouchX = event.rawX; initialTouchY = event.rawY; true }
                MotionEvent.ACTION_MOVE -> { params!!.x = initialX + (event.rawX - initialTouchX).toInt(); params!!.y = initialY + (event.rawY - initialTouchY).toInt(); windowManager.updateViewLayout(overlayView, params); true }
                MotionEvent.ACTION_UP -> { if (Math.abs(event.rawX - initialTouchX) < 10 && Math.abs(event.rawY - initialTouchY) < 10) startActivity(Intent(this, VoiceActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); true }
                else -> false
            }
        }
        windowManager.addView(overlayView, params)
    }

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onDestroy() { if (::overlayView.isInitialized) windowManager.removeView(overlayView); super.onDestroy() }
}
