package com.harsh.walkie_talkie.ui.presentation.home.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.ui.component.WonButton
import com.harsh.walkie_talkie.ui.presentation.home.model.User
import com.harsh.walkie_talkie.ui.presentation.home.viewmodel.UserViewModel
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import com.harsh.walkie_talkie.ui.theme.WHITE

@Composable
fun UserItem(
    user: User,
    userViewModel: UserViewModel,
    voice: (User) -> Unit,
    video: (User) -> Unit,
    text: (User) -> Unit,
) {
    var showDelete by remember { mutableStateOf(false) }

    if (showDelete)
        UserDeleteSheet(user, userViewModel) {
            showDelete = false
        }

    Row(
        modifier = Modifier
            .drawBehind {
                val strokeWidth = 1.dp.value * density
                val y = size.height - strokeWidth / 2
                drawLine(
                    LOW_OPACITY_WHITE,
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth
                )
            }
            .clickable {
                showDelete = true
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 30.dp, vertical = 10.dp)
        ) {
            Text(
                user.name.toString(),
                color = WHITE,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_extrabold)),
            )
            Text(
                user.uid.toString(),
                color = WHITE,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_medium)),
                fontStyle = FontStyle.Italic
            )
        }
        WonButton(icon = R.drawable.microphone, desc = "Voice Message") { voice(user) }
        WonButton(icon = R.drawable.video, desc = "Video Message") { video(user) }
        WonButton(icon = R.drawable.message, desc = "Text Message") { text(user) }
    }
}