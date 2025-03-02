package com.harsh.walkie_talkie.ui.presentation.rtc.text

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.speech.SpeechRecognizer
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.messaging.FirebaseMessaging
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.data.model.Profile
import com.harsh.walkie_talkie.network.rtc.initRTC
import com.harsh.walkie_talkie.service.WeechRecognizer
import com.harsh.walkie_talkie.service.stopListening
import com.harsh.walkie_talkie.ui.component.Background
import com.harsh.walkie_talkie.ui.component.BottomControl
import com.harsh.walkie_talkie.ui.component.PermissionControl
import com.harsh.walkie_talkie.ui.component.RTCNote
import com.harsh.walkie_talkie.ui.component.TopCard
import com.harsh.walkie_talkie.ui.component.WonButton
import com.harsh.walkie_talkie.ui.presentation.home.model.User
import com.harsh.walkie_talkie.ui.presentation.home.viewmodel.UserViewModel
import com.harsh.walkie_talkie.ui.presentation.receiver.MeetingInfo
import com.harsh.walkie_talkie.ui.presentation.rtc.ReceivedBroadcast
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import com.harsh.walkie_talkie.ui.theme.WHITE
import com.harsh.walkie_talkie.util.PreferencesHelper

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun TextMessagingScreen(
    navHostController: NavHostController,
    meetingInfo: MeetingInfo?,
    userViewModel: UserViewModel = viewModel()
) {

    val context = LocalContext.current
    var message by remember { mutableStateOf(TextFieldValue()) }
    var inviterToken by remember { mutableStateOf("") }
    var spokenText by remember { mutableStateOf("") }
    var listen by remember { mutableStateOf(false) }
    var isReceived by remember { mutableStateOf(false) }
    val myProfile = PreferencesHelper.getProfile(context, Profile::class.java)!!
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val user = meetingInfo!!.user!!
    val gotUser by userViewModel.findUser(user.uid).collectAsState(initial = null)

    ReceivedBroadcast("TEXT_FCM_MESSAGE") {
        message = TextFieldValue(it.meetingText!!)
        isReceived = true
    }

    LaunchedEffect(Unit) { message = TextFieldValue(meetingInfo.meetingText!!) }

    if (listen) WeechRecognizer(audioManager, speechRecognizer) {
        if (isReceived) spokenText = " ${it[0]}" else spokenText += " ${it[0]}"
        message = TextFieldValue(spokenText)
    } else stopListening(audioManager, speechRecognizer)

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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .border(
                            border = BorderStroke(1.dp, LOW_OPACITY_WHITE),
                            shape = RoundedCornerShape(18.dp)
                        )
                ) {
                    Text(
                        if (isReceived) "received message:" else "message:",
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
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            color = WHITE,
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        ),
                        onValueChange = {
                            message = it
                        },
                        keyboardActions = KeyboardActions.Default,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            autoCorrect = false,
                            imeAction = ImeAction.Done
                        ),
                    )
                    WonButton(
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .align(Alignment.End),
                        if (listen) R.drawable.microphone_slash else R.drawable.microphone,
                        desc = "Record Message"
                    ) { listen = !listen }
                }
                RTCNote()
            }
            BottomControl("To transmit message:", if (message.text.isNotEmpty()) "Send Note" else "Close Session") {
                if (message.text.isNotEmpty()) {
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result != null) {
                            inviterToken = task.result
                            initRTC(context, MeetingInfo(
                                meetingType = "text",
                                meetingText = message.text,
                                user = User(uid = myProfile.uid, name = "", token = myProfile.token)
                            ), user.token) {
                                message = TextFieldValue("")
                            }
                        }
                    }
                } else navHostController.popBackStack()
            }
        }
    }
}