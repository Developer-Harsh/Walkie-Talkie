package com.harsh.walkie_talkie.network

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.service.INTENT_COMMAND_NOTE
import com.harsh.walkie_talkie.ui.component.startFloatingService
import com.harsh.walkie_talkie.util.Constants.decodeMeetingInfo
import com.harsh.walkie_talkie.util.PreferencesHelper
import org.json.JSONObject
import kotlin.random.Random

class FcmService : FirebaseMessagingService() {

    private val channelId = "Messaging"
    private val channelName = "Realtime Communication"

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.data.let { data ->
            val jsonData = JSONObject(data["body"] ?: "{}")
            val meetingInfo = decodeMeetingInfo(jsonData.optString("content"))

            if (meetingInfo!!.meetingType == "noti") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel()
                }

                val builder = NotificationCompat.Builder(applicationContext, channelId)
                    .setSmallIcon(
                        IconCompat.createWithResource(
                            applicationContext,
                            R.drawable.logo_notification
                        )
                    )
                    .setColor(applicationContext.getColor(R.color.black))
                    .setContentText("Received message: ${meetingInfo.meetingText}")
                    .setBadgeIconType(R.drawable.ic_launcher_foreground)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setAutoCancel(true)
                    .setOngoing(true)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    with(NotificationManagerCompat.from(applicationContext)) {
                        if (ActivityCompat.checkSelfPermission(
                                applicationContext,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return
                        }
                        notify(Random.nextInt(3000), builder.build())
                    }
                } else {
                    NotificationManagerCompat.from(applicationContext)
                        .notify(Random.nextInt(3000), builder.build())
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    notificationManager.cancel(1)
                }, 5000)
            }

            PreferencesHelper.setObject(this, "meetInfo", meetingInfo)
            startFloatingService(INTENT_COMMAND_NOTE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH,
        )
        notificationManager.createNotificationChannel(channel)
    }
}