package com.harsh.walkie_talkie.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.harsh.walkie_talkie.ui.theme.CIRCLES_WHITE
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import com.harsh.walkie_talkie.ui.theme.WHITE

@Composable
fun WonButton(
    modifier: Modifier? = Modifier,
    icon: Int,
    desc: String? = "",
    start: Boolean? = false,
    noP: Boolean? = false,
    onClick: () -> Unit,
) {
    val sP = if (noP!!) 0.dp else if (start!!) 10.dp else 0.dp
    val eP = if (noP) 0.dp else if (start!!) 0.dp else 10.dp

    IconButton(
        colors = IconButtonColors(
            containerColor = LOW_OPACITY_WHITE,
            contentColor = WHITE,
            CIRCLES_WHITE,
            LOW_OPACITY_WHITE
        ),
        modifier = modifier!!.padding(end = eP, start = sP),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = desc,
            Modifier
                .padding(11.dp)
                .size(30.dp)
        )
    }
}