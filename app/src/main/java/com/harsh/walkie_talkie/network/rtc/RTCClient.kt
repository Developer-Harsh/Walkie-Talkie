package com.harsh.walkie_talkie.network.rtc

import android.content.Context
import android.widget.Toast
import com.harsh.walkie_talkie.data.model.Notification
import com.harsh.walkie_talkie.data.model.NotificationData
import com.harsh.walkie_talkie.network.ApiService
import com.harsh.walkie_talkie.ui.presentation.receiver.MeetingInfo
import com.harsh.walkie_talkie.util.Constants.encodeMeetingInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

fun initRTC(
    context: Context,
    meetingInfo: MeetingInfo,
    token: String,
    onSuccess: () -> Unit,
) {
    val data = JSONObject()
    data.put("content", encodeMeetingInfo(meetingInfo))

    val notification = Notification(
        message = NotificationData(
            token = token,
            data = hashMapOf(
                "title" to "Incoming message",
                "body" to data.toString()
            )
        )
    )

    sendRemoteMessage(context, notification) { onSuccess() }
}

private fun sendRemoteMessage(context: Context, remoteMessageBody: Notification, onSuccess: () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val result = ApiService().sendRemoteMessage(remoteMessageBody)
        withContext(Dispatchers.Main) {
            result.onSuccess {
                onSuccess()
            }.onFailure {
                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}