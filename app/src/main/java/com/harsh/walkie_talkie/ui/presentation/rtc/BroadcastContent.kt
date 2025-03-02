package com.harsh.walkie_talkie.ui.presentation.rtc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.harsh.walkie_talkie.ui.presentation.receiver.MeetingInfo
import com.harsh.walkie_talkie.util.Constants.decodeMeetingInfo

@Composable
fun ReceivedBroadcast(from: String, meetingInfo: (MeetingInfo) -> Unit) {

    val context = LocalContext.current

    DisposableEffect(Unit) {
        val receivedBroadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    meetingInfo(decodeMeetingInfo(intent.getStringExtra("content")!!)!!)
                }
            }
        }

        ContextCompat.registerReceiver(
            context,
            receivedBroadcast,
            IntentFilter("com.harsh.walkie_talkie.$from"),
            ContextCompat.RECEIVER_EXPORTED
        )

        onDispose {
            context.unregisterReceiver(receivedBroadcast)
        }
    }
}