@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)

package com.harsh.walkie_talkie.ui.component

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.service.FloatingService
import com.harsh.walkie_talkie.service.INTENT_COMMAND
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import com.harsh.walkie_talkie.ui.theme.WHITE

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun PermissionControl() {

    var isShow by remember { mutableStateOf(false) }
    var btnLabel by remember { mutableStateOf("") }

    val permissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE,
        )
    )

    LaunchedEffect(Unit) {
        if (!permissions.allPermissionsGranted) isShow = true
    }

    if (isShow) {
        ShowOverlayPermission {
            isShow = false
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp)
            .background(LOW_OPACITY_WHITE, RoundedCornerShape(18.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "We need some mandatory permission to work perfectly as you expected us to be.",
            color = WHITE,
            fontSize = 11.sp,
            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
            modifier = Modifier.weight(1f)
        )
        TextButton(
            onClick = {
                isShow = true
            },
            modifier = Modifier.padding(start = 10.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 15.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = WHITE,
                containerColor = LOW_OPACITY_WHITE
            ),
        ) {
            btnLabel = if (permissions.allPermissionsGranted) "Allowed" else "Allow"

            Text(
                btnLabel,
                color = WHITE,
                fontSize = 11.sp,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
            )
        }
    }
}

fun Context.startFloatingService(command: String = "") {
    val intent = Intent(this, FloatingService::class.java)
    if (command.isNotBlank()) intent.putExtra(INTENT_COMMAND, command)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.startForegroundService(intent)
    } else {
        this.startService(intent)
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun ShowOverlayPermission(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val permissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE,
        )
    )

    ModalBottomSheet(
        modifier = Modifier.height(550.dp),
        containerColor = Color.Transparent,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        dragHandle = { },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Image(
                painter = painterResource(R.drawable.background),
                modifier = Modifier
                    .fillMaxWidth()
                    .blur(10.dp),
                contentScale = ContentScale.Crop,
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.6f), BlendMode.Overlay)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
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
                        .padding(horizontal = 40.dp)
                        .padding(top = 40.dp)
                ) {
                    Text(
                        "Allow permission:",
                        color = WHITE,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_bold)),
                    )
                    Text(
                        "These permissions are required to receive messages from other walkie-talkies, and to make fully functional app.",
                        color = WHITE,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Text(
                        "What are the permissions?",
                        color = WHITE,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_semibold)),
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Text(
                        "• Camera Permission - Used for Video Broadcast.\n" +
                                "• Audio Permission - Used for Voice Messaging.\n" +
                                "• Notification Permission - Used for Displaying Notification.\n" +
                                "• Battery Optimization - Used for Realtime\n   Communication Environment.\n" +
                                "• System UI Overlay - Used for Communication.",
                        color = WHITE,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 40.dp)
                        .padding(bottom = 40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Please permit us 😥:",
                        color = WHITE,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = {
                            if (permissions.allPermissionsGranted) {
                                if (isIgnoringBatteryOptimizations(context))
                                    requestBatteryOptimization(context)
                                else if (!Settings.canDrawOverlays(context)) {
                                    context.startActivity(
                                        Intent(
                                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                            Uri.parse("package:" + context.packageName)
                                        )
                                    )
                                } else {
                                    onDismiss()
                                }
                            } else {
                                permissions.launchMultiplePermissionRequest()
                            }
                        },
                        modifier = Modifier.padding(start = 10.dp),
                        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 18.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = WHITE,
                            containerColor = LOW_OPACITY_WHITE
                        ),
                    ) {
                        Text(
                            "Grant",
                            color = WHITE,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_bold)),
                        )
                    }
                }
            }
        }
    }
}

fun isIgnoringBatteryOptimizations(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    return powerManager.isIgnoringBatteryOptimizations(context.packageName)
}

fun requestBatteryOptimization(context: Context) {
    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
    intent.data = Uri.parse("package:${context.packageName}")
    context.startActivity(intent)
}