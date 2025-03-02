package com.harsh.walkie_talkie.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.app.ServiceCompat
import androidx.lifecycle.setViewTreeLifecycleOwner
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.ui.presentation.receiver.MeetingInfo
import com.harsh.walkie_talkie.ui.presentation.receiver.MessageSheet
import com.harsh.walkie_talkie.util.PreferencesHelper
import com.harsh.walkie_talkie.util.WindowLifecycleOwner

const val INTENT_COMMAND = "com.harsh.walkie.COMMAND"
const val INTENT_COMMAND_EXIT = "Exit"
const val INTENT_COMMAND_NOTE = "Message"

private const val NOTIFICATION_CHANNEL_GENERAL = "Floating Window"
private const val CODE_FOREGROUND_SERVICE = 1
private const val CODE_EXIT_INTENT = 2
private const val CODE_NOTE_INTENT = 3

class FloatingService : Service() {
    private val lifecycleOwner = WindowLifecycleOwner()

    override fun onCreate() {
        super.onCreate()
        lifecycleOwner.onCreate()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun stopService() {
        lifecycleOwner.onStop()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun showNotification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val exitIntent = Intent(this, FloatingService::class.java).apply {
            putExtra(INTENT_COMMAND, INTENT_COMMAND_EXIT)
        }

        val noteIntent = Intent(this, FloatingService::class.java).apply {
            putExtra(INTENT_COMMAND, INTENT_COMMAND_NOTE)
        }

        val exitPendingIntent = PendingIntent.getService(
            this, CODE_EXIT_INTENT, exitIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notePendingIntent = PendingIntent.getService(
            this, CODE_NOTE_INTENT, noteIntent, PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                with(
                    NotificationChannel(
                        NOTIFICATION_CHANNEL_GENERAL,
                        "Floating Service",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                ) {
                    enableLights(false)
                    setShowBadge(false)
                    enableVibration(false)
                    setSound(null, null)
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                    manager.createNotificationChannel(this)
                }
            }
        } catch (ignored: Exception) {
            // Ignore exception.
        }

        with(
            NotificationCompat.Builder(
                this,
                NOTIFICATION_CHANNEL_GENERAL
            )
        ) {
            setTicker(null)
            setContentText("This service is required to receive messages, helping users to stay connected.")
            setAutoCancel(false)
            setOngoing(true)
            setWhen(System.currentTimeMillis())
            setSmallIcon(R.drawable.logo_notification)
            setPriority(PRIORITY_HIGH)
            setContentIntent(notePendingIntent)
            addAction(
                NotificationCompat.Action(
                    0,
                    "Close",
                    exitPendingIntent
                )
            )

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(CODE_FOREGROUND_SERVICE, build())
            } else {
                ServiceCompat.startForeground(this@FloatingService, CODE_FOREGROUND_SERVICE, build(), FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val command = intent?.getStringExtra(INTENT_COMMAND)
        if (command == INTENT_COMMAND_EXIT) {
            stopService()
            return START_NOT_STICKY
        }

        showNotification()
        window()

        if (command == INTENT_COMMAND_NOTE) {
            open()
        }

        return START_STICKY
    }

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View

    private val params = WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            WindowManager.LayoutParams.TYPE_PHONE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    ).apply {
        gravity = Gravity.BOTTOM or Gravity.CENTER
    }

    private fun window() {
        if (Settings.canDrawOverlays(this@FloatingService)) {
            if (::windowManager.isInitialized && floatingView != null) {
                return // Prevent duplicate overlays
            }

            lifecycleOwner.onStart()

            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
            val layoutInflater = LayoutInflater.from(this)
            floatingView = layoutInflater.inflate(R.layout.received_window, null)

            lifecycleOwner.attachToDecorView(floatingView)

            val composeView = floatingView.findViewById<ComposeView>(R.id.compose_view)
            composeView.apply {
                setViewTreeLifecycleOwner(lifecycleOwner)
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MessageSheet(meetingInfo = PreferencesHelper.getObject(this@FloatingService, "meetInfo", MeetingInfo::class.java)!!) {
                        lifecycleOwner.onStop()
                        close()
                    }
                }
            }
        }
    }

    private fun open() {
        try {
            windowManager.addView(floatingView, params)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to open floating window", Toast.LENGTH_SHORT).show()
        }
    }

    private fun close() {
        try {
            windowManager.removeView(floatingView)
        } catch (e: Exception) {
            // Ignore
        }
    }
}