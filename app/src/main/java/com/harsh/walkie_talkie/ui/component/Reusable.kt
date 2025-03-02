package com.harsh.walkie_talkie.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import com.harsh.walkie_talkie.ui.theme.WHITE

@Composable
fun RTCNote(isText: Boolean? = true) {
    Text(
        "You can ${if (isText!!) "write" else "say"} whatever you want, we will make sure to deliver your message to the person you wanna share.",
        color = WHITE,
        fontSize = 14.sp,
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .padding(top = 30.dp),
        fontStyle = FontStyle.Italic,
        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_light)),
    )
}

@Composable
fun Background(
    colorFilter: Boolean? = false,
    blur: Boolean? = true,
) {
    Image(
        painter = painterResource(R.drawable.background),
        modifier = Modifier
            .fillMaxSize()
            .blur(if (blur!!) 6.dp else 0.dp),
        contentScale = ContentScale.Crop,
        contentDescription = "",
        colorFilter = ColorFilter.tint(if (colorFilter!!) Color.Black.copy(alpha = 0.6f) else Color.Transparent, BlendMode.Overlay)
    )
}

@Composable
fun ButtonRecActivity(modifier: Modifier? = Modifier, text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = modifier!!,
        contentPadding = PaddingValues(horizontal = 30.dp, vertical = 18.dp),
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = WHITE,
            containerColor = LOW_OPACITY_WHITE
        ),
    ) {
        Text(
            text,
            color = WHITE,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_bold)),
        )
    }
}