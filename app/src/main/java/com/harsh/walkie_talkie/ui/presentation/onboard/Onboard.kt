package com.harsh.walkie_talkie.ui.presentation.onboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.network.ApiService
import com.harsh.walkie_talkie.ui.component.Background
import com.harsh.walkie_talkie.ui.component.RippleBox
import com.harsh.walkie_talkie.ui.component.SwipeToStartButton
import com.harsh.walkie_talkie.ui.navigation.Pages
import com.harsh.walkie_talkie.ui.theme.WHITE
import kotlinx.coroutines.launch

@Composable
fun OnboardScreen(navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Background(blur = false)
        Column {
            RippleBox(modifier = Modifier.fillMaxWidth().padding(top = 100.dp))
            Column(
                modifier = Modifier.padding(horizontal = 50.dp).padding(top = 50.dp)
            ) {
                Text(
                    "Continue to app",
                    color = WHITE,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_bold)),
                )
                Text(
                    "Here, by using the walkie-talkie app, you can send and receive messages in real-time. It works similarly to a walkie-talkie, allowing you to send video, voice, or text messages. The sent message will be shown on the target device in real-time.",
                    color = WHITE,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    modifier = Modifier.padding(top = 10.dp)
                )
                SwipeToStartButton(modifier = Modifier.padding(top = 60.dp), onSwipeComplete = {
                    navHostController.navigate(Pages.Home.route)
                    scope.launch {
                        ApiService().saveProfile(context = context)
                        navHostController.navigate(Pages.Home.route)
                    }
                })
                Text(
                    "By using our app you will agree to our Terms of Service and Privacy Policy.",
                    color = WHITE,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_light)),
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }
    }
}