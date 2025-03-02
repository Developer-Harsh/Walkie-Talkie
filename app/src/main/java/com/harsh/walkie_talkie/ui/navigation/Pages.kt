package com.harsh.walkie_talkie.ui.navigation

sealed class Pages(val route: String) {
    data object Splash : Pages("splash")
    data object Onboard : Pages("onboard")
    data object Home : Pages("home")
    data object TextMessaging : Pages("text_messaging")
    data object VideoMessaging : Pages("video_messaging")
    data object VoiceMessaging : Pages("voice_messaging")
}