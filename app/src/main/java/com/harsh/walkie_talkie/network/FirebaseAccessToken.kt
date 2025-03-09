package com.harsh.walkie_talkie.network

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.harsh.walkie_talkie.util.Constants
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

object FirebaseAccessToken {
    fun getAccessToken(): String? {
        try {
            val json = "YOUR_FIREBASE_KEY_PAIR"

            val stream = ByteArrayInputStream(json.toByteArray(StandardCharsets.UTF_8))
            val googleCreds = GoogleCredentials.fromStream(stream).createScoped(arrayListOf(
                Constants.FCM_SCOPE))
            googleCreds.refresh()

            return googleCreds.accessToken.tokenValue
        } catch (e: IOException) {
            Log.e("TAG", e.message.toString())
            return null
        }
    }
}
