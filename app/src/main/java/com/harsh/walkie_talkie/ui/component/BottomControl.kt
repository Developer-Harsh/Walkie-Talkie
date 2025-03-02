package com.harsh.walkie_talkie.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import com.harsh.walkie_talkie.ui.theme.WHITE

@Composable
fun BottomControl(
    label: String,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 30.dp)
            .padding(bottom = 50.dp, top = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            color = WHITE,
            fontSize = 13.sp,
            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
            modifier = Modifier.weight(1f)
        )
        TextButton(
            onClick = onClick,
            modifier = Modifier.padding(start = 10.dp),
            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 18.dp),
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
}