package com.harsh.walkie_talkie.ui.presentation.home.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.ui.component.Background
import com.harsh.walkie_talkie.ui.presentation.home.model.User
import com.harsh.walkie_talkie.ui.presentation.home.viewmodel.UserViewModel
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import com.harsh.walkie_talkie.ui.theme.WHITE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDeleteSheet(
    user: User,
    userViewModel: UserViewModel,
    onDismiss: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier.height(300.dp),
        containerColor = Color.Transparent,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        dragHandle = { },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                        .padding(horizontal = 40.dp)
                        .padding(top = 40.dp)
                ) {
                    Text(
                        "You saved this contact",
                        color = WHITE,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_bold)),
                    )
                    Text(
                        "Here are the details for this contact below:\n" +
                                "• Nickname: ${user.name}.\n" +
                                "• UID: ${user.uid}.\n",
                        color = WHITE,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
                TextButton(
                    onClick = {
                        userViewModel.deleteUser(user)
                        onDismiss()
                    },
                    modifier = Modifier.padding(bottom = 40.dp),
                    contentPadding = PaddingValues(horizontal = 35.dp, vertical = 18.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = WHITE,
                        containerColor = LOW_OPACITY_WHITE
                    ),
                ) {
                    Text(
                        "Delete",
                        color = WHITE,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_bold)),
                    )
                }
            }
        }
    }
}