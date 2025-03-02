package com.harsh.walkie_talkie.ui.presentation.rtc.voice

import android.os.Build
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.messaging.FirebaseMessaging
import com.harsh.walkie_talkie.data.model.Profile
import com.harsh.walkie_talkie.network.rtc.initRTC
import com.harsh.walkie_talkie.ui.component.Background
import com.harsh.walkie_talkie.ui.component.BottomControl
import com.harsh.walkie_talkie.ui.component.PermissionControl
import com.harsh.walkie_talkie.ui.component.RTCNote
import com.harsh.walkie_talkie.ui.component.RippleBox
import com.harsh.walkie_talkie.ui.component.TopCard
import com.harsh.walkie_talkie.ui.presentation.home.model.User
import com.harsh.walkie_talkie.ui.presentation.home.viewmodel.UserViewModel
import com.harsh.walkie_talkie.ui.presentation.receiver.MeetingInfo
import com.harsh.walkie_talkie.util.Constants.CALL_URL
import com.harsh.walkie_talkie.util.Constants.genMID
import com.harsh.walkie_talkie.util.PreferencesHelper

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun VoiceMessagingScreen(
    navHostController: NavHostController,
    meetingInfo: MeetingInfo?,
    userViewModel: UserViewModel = viewModel()
) {

    val context = LocalContext.current
    val user = meetingInfo!!.user!!
    val gotUser by userViewModel.findUser(user.uid).collectAsState(initial = null)
    var inviterToken by remember { mutableStateOf("") }
    val meetingRoom = genMID()
    val myProfile = PreferencesHelper.getProfile(context, Profile::class.java)!!

    FirebaseMessaging.getInstance().getToken().addOnCompleteListener { task ->
        if (task.isSuccessful && task.result != null) {
            inviterToken = task.result
            initRTC(
                context, MeetingInfo(
                    meetingType = "video",
                    meetingRoom = meetingRoom,
                    user = User(uid = myProfile.uid, name = "", token = myProfile.token)
                ), user.token
            ) {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Background(blur = false)
        Column {
            val name = user.name.takeIf { it.isNotEmpty() }
                ?: gotUser?.name?.takeIf { it.isNotEmpty() }
                ?: "Unknown"

            TopCard(
                user.uid,
                mine = false,
                nickname = name
            )
            PermissionControl()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
                    .weight(1f),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0f),
                        factory = {
                            WebView(context).apply {
                                settings.apply {
                                    javaScriptEnabled = true
                                    domStorageEnabled = true
                                    mediaPlaybackRequiresUserGesture = false
                                    allowFileAccessFromFileURLs = true
                                    allowUniversalAccessFromFileURLs = true
                                    allowContentAccess = true
                                }

                                webChromeClient = object : WebChromeClient() {
                                    override fun onPermissionRequest(request: PermissionRequest?) {
                                        request?.grant(request.resources)
                                    }
                                }

                                webViewClient = object : WebViewClient() {
                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        post {
                                            view?.evaluateJavascript("streamId = '$meetingRoom'; window.init();") {
                                            }
                                        }
                                    }
                                }

                                loadUrl(CALL_URL)
                            }
                        },
                        update = {
                            it.loadUrl(CALL_URL)
                            it.evaluateJavascript("streamId = '$meetingRoom'; window.init();") {
                            }
                        },
                    )
                    RippleBox(modifier = Modifier.padding(top = 20.dp), size = 250.dp)
                }
            }
            RTCNote()
            BottomControl("To end session:", "Close Session") {
                navHostController.popBackStack()
            }
        }
    }
}