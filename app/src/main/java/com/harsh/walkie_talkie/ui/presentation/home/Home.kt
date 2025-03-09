package com.harsh.walkie_talkie.ui.presentation.home

import android.os.Build
import android.os.StrictMode
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.messaging.FirebaseMessaging
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.data.model.Profile
import com.harsh.walkie_talkie.network.ApiService
import com.harsh.walkie_talkie.ui.component.Background
import com.harsh.walkie_talkie.ui.component.BottomControl
import com.harsh.walkie_talkie.ui.component.PermissionControl
import com.harsh.walkie_talkie.ui.component.TopCard
import com.harsh.walkie_talkie.ui.navigation.Pages
import com.harsh.walkie_talkie.ui.presentation.contact.AddContactSheet
import com.harsh.walkie_talkie.ui.presentation.contact.FoundContactSheet
import com.harsh.walkie_talkie.ui.presentation.home.list.UserItem
import com.harsh.walkie_talkie.ui.presentation.home.model.User
import com.harsh.walkie_talkie.ui.presentation.home.viewmodel.UserViewModel
import com.harsh.walkie_talkie.ui.presentation.receiver.MeetingInfo
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import com.harsh.walkie_talkie.ui.theme.WHITE
import com.harsh.walkie_talkie.util.Constants.encodeMeetingInfo
import com.harsh.walkie_talkie.util.PreferencesHelper
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun HomeScreen(navHostController: NavHostController, userViewModel: UserViewModel = viewModel()) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val users by userViewModel.users.observeAsState(emptyList())
    var showSheet by remember { mutableStateOf(false) }
    var showSaveSheet by remember { mutableStateOf(false) }
    var foundUser by remember { mutableStateOf(User(name = "", uid = "", token = "")) }

    val profile by remember {
        mutableStateOf(
            PreferencesHelper.getProfile(
                context,
                Profile::class.java
            )
        )
    }

    if (showSheet) {
        AddContactSheet { user, bool ->
            foundUser = user
            showSaveSheet = bool
            showSheet = false
        }
    }

    if (showSaveSheet)
        FoundContactSheet(foundUser, userViewModel) {
            showSaveSheet = false
        }

    Box(modifier = Modifier.fillMaxSize()) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                scope.launch {
                    ApiService().updateFirebase(
                        context,
                        profile?.uid.toString(),
                        mapOf("token" to it.result)
                    )
                }
            }
        }

        Background(blur = false)

        Column {
            TopCard(
                profile?.uid.toString(),
            )
            PermissionControl()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
                    .background(LOW_OPACITY_WHITE, RoundedCornerShape(18.dp))
                    .weight(1f),
            ) {
                items(users.size) {
                    UserItem(users[it], userViewModel, voice = { profile ->
                        navHostController.navigate(
                            Pages.VoiceMessaging.route + encodeMeetingInfo(
                                MeetingInfo(user = profile)
                            )
                        )
                    }, video = { profile ->
                        navHostController.navigate(
                            Pages.VideoMessaging.route + encodeMeetingInfo(
                                MeetingInfo(user = profile)
                            )
                        )
                    }) { profile ->
                        navHostController.navigate(
                            Pages.TextMessaging.route + encodeMeetingInfo(
                                MeetingInfo(user = profile)
                            )
                        )
                    }
                }
                item {
                    Text(
                        "No more found",
                        color = WHITE,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_light)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp),
                    )
                }
            }

            BottomControl("You can add contacts:", "Add Contact") {
                showSheet = true
            }
        }
    }
}