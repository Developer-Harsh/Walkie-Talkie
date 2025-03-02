package com.harsh.walkie_talkie.ui.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.ui.component.Background
import com.harsh.walkie_talkie.ui.navigation.Pages
import com.harsh.walkie_talkie.ui.theme.WHITE
import com.harsh.walkie_talkie.util.PreferencesHelper
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navHostController: NavHostController) {

    val context = LocalContext.current

    LaunchedEffect(true) {
        delay(3000)
        val route = if (PreferencesHelper.getSettled(context)) Pages.Home.route else Pages.Onboard.route
        navHostController.navigate(route) {
            popUpTo(0)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Background(blur = false)
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 260.dp).padding(horizontal = 55.dp).fillMaxWidth()
        ) {
            Text(
                "walkie\nTALKIE",
                color = WHITE,
                fontSize = 44.sp,
                fontFamily = FontFamily(Font(R.font.good_time)),
                lineHeight = 46.sp,
            )
            Text(
                "Real-time Communication | Voice | Text | Video",
                color = WHITE,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_light)),
                modifier = Modifier.padding(top = 3.dp)
            )
        }
        Text(
            "Made in India \uD83C\uDDEE\uD83C\uDDF3",
            color = WHITE,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_light)),
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 50.dp)
        )
    }
}