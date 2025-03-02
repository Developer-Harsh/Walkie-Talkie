package com.harsh.walkie_talkie.ui.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import com.harsh.walkie_talkie.ui.theme.WHITE

@Composable
fun TopCard(
    id: String,
    nickname: String? = "",
    mine: Boolean? = true,
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp)
            .padding(top = 20.dp)
            .background(LOW_OPACITY_WHITE, RoundedCornerShape(18.dp))
            .padding(20.dp)
    ) {
        Text(
            if (mine!!) "Hello, dear app user! Here is your app account ID. To initiate communication, you must use this ID." else "Communication with a contact credentials shown below,  the name of contact is $nickname.",
            color = WHITE,
            fontSize = 13.sp,
            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
        )
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().weight(1f)) {
                Text(
                    "ID: $id",
                    color = WHITE,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_extrabold)),
                    modifier = Modifier.padding(top = 15.dp)
                )
                Text(
                    if (mine) "Your Account ID" else nickname.toString(),
                    color = WHITE,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            WonButton(icon = R.drawable.copy, desc = "Copy ID", start = true, modifier = Modifier.padding(top = 30.dp)) {
                clipboardManager.setText(AnnotatedString("Hey, I'm here on Walkie-Talkie.\nMine ID: $id"))
                Toast.makeText(context, "ID copied!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}