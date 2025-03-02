package com.harsh.walkie_talkie.util

import com.harsh.walkie_talkie.ui.presentation.receiver.MeetingInfo
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

object Constants {
    const val FCM_SCOPE: String = "https://www.googleapis.com/auth/firebase.messaging"
    const val BASE_URL: String = "https://d52d-2409-40d2-5a-eb05-8170-e0fa-7a73-c32c.ngrok-free.app/"
    const val CALL_URL: String = "${BASE_URL}index.html"
    const val VIEW_URL: String = "${BASE_URL}viewer.html"

    fun genUid(): String {
        val part1 = (100..999).random()
        val part2 = (1000..9999).random()
        val part3 = (100..999).random()
        return "$part1-$part2-$part3"
    }

    fun genMID(): String {
        val part1 = (100..999).random()
        val part2 = (1000..9999).random()
        val part3 = (100..999).random()
        val part4 = (100..9999).random()
        return "$part1-$part2-$part3-$part4-harsh"
    }

    fun encodeMeetingInfo(meetingInfo: MeetingInfo): String {
        return Json.encodeToString(meetingInfo).let { URLEncoder.encode(it, "UTF-8") }
    }

    fun decodeMeetingInfo(encodedString: String): MeetingInfo? {
        return try {
            Json.decodeFromString<MeetingInfo>(URLDecoder.decode(encodedString, "UTF-8"))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}