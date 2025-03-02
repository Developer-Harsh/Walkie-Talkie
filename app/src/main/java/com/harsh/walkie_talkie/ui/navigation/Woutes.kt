package com.harsh.walkie_talkie.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.harsh.walkie_talkie.ui.presentation.home.HomeScreen
import com.harsh.walkie_talkie.ui.presentation.onboard.OnboardScreen
import com.harsh.walkie_talkie.ui.presentation.rtc.text.TextMessagingScreen
import com.harsh.walkie_talkie.ui.presentation.rtc.video.VideoMessagingScreen
import com.harsh.walkie_talkie.ui.presentation.rtc.voice.VoiceMessagingScreen
import com.harsh.walkie_talkie.ui.presentation.splash.SplashScreen
import kotlinx.serialization.json.Json
import java.net.URLDecoder

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun Woutes(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Pages.Splash.route
    ) {
        composable(route = Pages.Splash.route) {
            SplashScreen(navHostController = navController)
        }
        composable(route = Pages.Onboard.route) {
            OnboardScreen(navHostController = navController)
        }
        composable(route = Pages.Home.route) {
            HomeScreen(navHostController = navController)
        }
        composable(route = Pages.TextMessaging.route + "{meetingInfo}",
            arguments = listOf(navArgument("meetingInfo") { type = NavType.StringType })
        ) { bk ->
            TextMessagingScreen(navHostController = navController, meetingInfo = backStackData(bk = bk, key = "meetingInfo"))
        }
        composable(route = Pages.VideoMessaging.route + "{meetingInfo}",
            arguments = listOf(navArgument("meetingInfo") { type = NavType.StringType })
        ) { bk ->
            VideoMessagingScreen(navHostController = navController, meetingInfo = backStackData(bk = bk, key = "meetingInfo"))
        }
        composable(route = Pages.VoiceMessaging.route + "{meetingInfo}",
            arguments = listOf(navArgument("meetingInfo") { type = NavType.StringType })
        ) { bk ->
            VoiceMessagingScreen(navHostController = navController, meetingInfo = backStackData(bk = bk, key = "meetingInfo"))
        }
    }
}

inline fun <reified T> backStackData(bk: NavBackStackEntry, key: String): T? {
    val json = bk.arguments?.getString(key)?.takeIf { it.isNotEmpty() }?.let { URLDecoder.decode(it, "UTF-8") }
    return json?.let { Json.decodeFromString<T>(it) }
}

//fun backStackData(bk: NavBackStackEntry): User? {
//    val json = bk.arguments?.getString("user")?.let { URLDecoder.decode(it, "UTF-8") }
//    return json?.let { Json.decodeFromString<User>(it) }
//}