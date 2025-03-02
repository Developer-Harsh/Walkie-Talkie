package com.harsh.walkie_talkie

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.harsh.walkie_talkie.ui.navigation.Pages
import com.harsh.walkie_talkie.ui.navigation.Woutes
import com.harsh.walkie_talkie.ui.presentation.rtc.ReceivedBroadcast
import com.harsh.walkie_talkie.ui.theme.WalkieTalkieTheme
import com.harsh.walkie_talkie.util.AppLifecycleObserver
import com.harsh.walkie_talkie.util.Constants.encodeMeetingInfo

@RequiresApi(Build.VERSION_CODES.P)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycle.addObserver(AppLifecycleObserver())

        val type = intent.getStringExtra("type") ?: "N/A"
        val data = intent.getStringExtra("data") ?: "N/A"

        setContent {
            MainApp(type, data)
        }
    }
}

@Composable
fun MainApp(type: String, data: String) {
    WalkieTalkieTheme {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val nvc = rememberNavController()
            val context = LocalContext.current
            Woutes(navController = nvc)

            if (type != "N/A" && data != "N/A")
                when (type) {
                    "text" -> nvc.navigate(Pages.TextMessaging.route + data)
                    "voice" -> nvc.navigate(Pages.VoiceMessaging.route + data)
                    "video" -> nvc.navigate(Pages.VideoMessaging.route + data)
                }
            else
                ReceivedBroadcast("NEW_FCM_MESSAGE") {
                    if (nvc.currentDestination!!.route != Pages.TextMessaging.route)
                        nvc.navigate(Pages.TextMessaging.route + encodeMeetingInfo(it))
                    else
                        context.sendBroadcast(Intent("com.harsh.walkie_talkie.TEXT_FCM_MESSAGE").apply {
                            putExtra("content", encodeMeetingInfo(it))
                        })
                }
        }
    }
}