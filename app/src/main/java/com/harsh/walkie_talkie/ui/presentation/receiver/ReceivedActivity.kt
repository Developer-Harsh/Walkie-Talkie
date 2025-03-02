package com.harsh.walkie_talkie.ui.presentation.receiver

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.harsh.walkie_talkie.MainActivity
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.ui.component.Background
import com.harsh.walkie_talkie.ui.component.ButtonRecActivity
import com.harsh.walkie_talkie.ui.component.RTCNote
import com.harsh.walkie_talkie.ui.component.RippleBox
import com.harsh.walkie_talkie.ui.component.WonButton
import com.harsh.walkie_talkie.ui.presentation.home.model.User
import com.harsh.walkie_talkie.ui.presentation.home.viewmodel.UserViewModel
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import com.harsh.walkie_talkie.ui.theme.WHITE
import com.harsh.walkie_talkie.util.Constants.VIEW_URL
import com.harsh.walkie_talkie.util.Constants.encodeMeetingInfo
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class MeetingInfo(
    val meetingType: String? = "",
    val meetingRoom: String? = "",
    val meetingText: String? = "",
    val user: User? = User(uid = "", name = "", token = ""),
)

@Composable
fun MessageSheet(
    userViewModel: UserViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory(LocalContext.current.applicationContext as Application)
    ),
    meetingInfo: MeetingInfo,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var type by remember { mutableStateOf("text") }
    val user by userViewModel.findUser(meetingInfo.user!!.uid).collectAsState(initial = null)

    LaunchedEffect(meetingInfo.meetingType) { type = meetingInfo.meetingType.toString() }

    Box(
        modifier = Modifier
            .padding(top = 100.dp)
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
    ) {
        Background(true)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(100.dp)
                    .height(5.dp)
                    .background(WHITE.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 30.dp)
                    .padding(top = 40.dp)
            ) {
                Text(
                    "You have received a message",
                    color = WHITE,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_bold)),
                )
                val name = if (user != null && user!!.name.isNotEmpty()) user?.name else "someone not found in your contact"
                Text(
                    "Here is the message received from $name, ID : ${meetingInfo.user!!.uid}.",
                    color = WHITE,
                    fontSize = 13.sp,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    modifier = Modifier.padding(top = 10.dp, bottom = 15.dp)
                )
                when (type) {
                    "text" -> TextMessage(meetingInfo.meetingText.toString(), modifier = Modifier.weight(1f))
                    "voice" -> VoiceMessage(meetingInfo.meetingRoom.toString(), modifier = Modifier.weight(1f))
                    "video" -> VideoMessage(meetingInfo.meetingRoom.toString(), modifier = Modifier.weight(1f))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp, top = 20.dp, end = 30.dp)
                    .align(Alignment.End),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (expanded)
                    Row(
                        modifier = Modifier
                            .background(
                                LOW_OPACITY_WHITE,
                                RoundedCornerShape(50.dp)
                            )
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WonButton(icon = R.drawable.video, noP = true, desc = "Video Message") {
                            context.startActivity(Intent(context, MainActivity::class.java).apply {
                                putExtra("data", encodeMeetingInfo(meetingInfo))
                                putExtra("type", "video")
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            })
                            onDismiss()
                        }
                        WonButton(icon = R.drawable.microphone, noP = true, desc = "Voice Message") {
                            context.startActivity(Intent(context, MainActivity::class.java).apply {
                                putExtra("data", encodeMeetingInfo(meetingInfo))
                                putExtra("type", "voice")
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            })
                            onDismiss()
                        }
                        WonButton(icon = R.drawable.message, noP = true, desc = "Text Message") {
                            context.startActivity(Intent(context, MainActivity::class.java).apply {
                                putExtra("data", encodeMeetingInfo(meetingInfo))
                                putExtra("type", "text")
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            })
                            onDismiss()
                        }
                        WonButton(icon = R.drawable.close, noP = true, desc = "Close Popup") {
                            expanded = !expanded
                        }
                    }
                else ButtonRecActivity(text = "Reply") {
                    expanded = !expanded
                }
                ButtonRecActivity(text = "Close", modifier = Modifier.padding(start = 15.dp)) { onDismiss() }
            }
        }
    }
}

@Suppress("DEPRECATION")
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VoiceMessage(meetingRoom: String, modifier: Modifier? = Modifier) {
    val context = LocalContext.current
    var visibility by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(10000)
        visibility = true
    }

    Column(
        modifier = modifier!!
            .fillMaxSize()
            .clip(RoundedCornerShape(18.dp))
            .border(
                border = BorderStroke(1.dp, LOW_OPACITY_WHITE),
                shape = RoundedCornerShape(18.dp)
            ),
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (visibility)
                AndroidView(
                    modifier = Modifier.fillMaxSize().alpha(0f),
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

                            loadUrl(VIEW_URL)
                        }
                    },
                )
            RippleBox(modifier = Modifier.padding(top = 20.dp), size = 250.dp)
        }
        RTCNote(false)
    }
}

@Suppress("DEPRECATION")
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VideoMessage(meetingRoom: String, modifier: Modifier? = Modifier) {
    val context = LocalContext.current
    var progressShow by remember { mutableStateOf(true) }
    var visibility by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(10000)
        visibility = true
    }

    Column(
        modifier = modifier!!
            .fillMaxSize()
            .clip(RoundedCornerShape(18.dp))
            .border(
                border = BorderStroke(1.dp, LOW_OPACITY_WHITE),
                shape = RoundedCornerShape(18.dp)
            ),
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (progressShow)
                RippleBox(modifier = Modifier.padding(top = 20.dp), size = 250.dp)

            if (visibility)
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (progressShow) 0f else 1f),
                    factory = {
                        WebView(context).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.mediaPlaybackRequiresUserGesture = false
                            settings.allowFileAccessFromFileURLs = true
                            settings.allowUniversalAccessFromFileURLs = true

                            webChromeClient = object : WebChromeClient() {
                                override fun onPermissionRequest(request: PermissionRequest?) {
                                    super.onPermissionRequest(request)
                                    request!!.grant(request.resources)
                                }
                            }

                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    post {
                                        progressShow = false
                                        view?.evaluateJavascript("streamId = '$meetingRoom'; window.init();") {
                                        }
                                    }
                                }
                            }

                            loadUrl(VIEW_URL)
                        }
                    },
                    update = {
                        it.loadUrl(VIEW_URL)
                        it.evaluateJavascript("streamId = '$meetingRoom'; window.init();") {
                        }
                    },
                )
        }
        RTCNote(false)
    }
}

@Composable
fun TextMessage(rec: String, modifier: Modifier? = Modifier) {

    val context = LocalContext.current
    var message by remember { mutableStateOf(TextFieldValue(rec)) }
    var isSpeaking by remember { mutableStateOf(false) }
    val tts = remember { TextToSpeech(context) {} }

    LaunchedEffect(tts) { tts.setLanguage(Locale("hi_IN")) }

    Column(
        modifier = modifier!!
            .fillMaxSize()
            .border(
                border = BorderStroke(1.dp, LOW_OPACITY_WHITE),
                shape = RoundedCornerShape(18.dp)
            )
    ) {
        Text(
            "message:",
            color = WHITE,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp),
            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_light)),
        )
        BasicTextField(
            value = message,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 20.dp),
            keyboardActions = KeyboardActions.Default,
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = WHITE,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
            ),
            onValueChange = {
                message = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            WonButton(icon = if (!isSpeaking) R.drawable.volume_off else R.drawable.volume, start = true, desc = "Speak Message") {
                isSpeaking = !isSpeaking

                if (tts.isSpeaking)
                    tts.stop()
                else
                    tts.speak(message.text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            WonButton(icon = R.drawable.close, desc = "Clear Message") {
                message = TextFieldValue("")
            }
        }
    }
}

@Preview
@Composable
fun PrevMyScreen() {
    MessageSheet(meetingInfo = MeetingInfo()) { }
}