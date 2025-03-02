package com.harsh.walkie_talkie.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.ui.theme.CIRCLES_WHITE
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import com.harsh.walkie_talkie.ui.theme.WHITE

@Composable
fun KeypadButton(
    background: Color? = LOW_OPACITY_WHITE,
    contentColor: Color? = WHITE,
    index: Int? = 0,
    icon: Int? = 0,
    text: String? = "1",
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(if (index == 9) Color.Transparent else if (index == 11) CIRCLES_WHITE else background!!)
            .clickable {onClick()},
        contentAlignment = Alignment.Center
    ) {
        if (icon == 0) {
            Text(
                text = if (index == 9) "" else if (index == 11) "⌫" else text!!,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_extrabold)),
                color = contentColor!!
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.message),
                tint = contentColor!!,
                contentDescription = "Keypad Button"
            )
        }
    }
}