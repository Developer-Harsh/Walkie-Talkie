package com.harsh.walkie_talkie.ui.presentation.contact

import android.annotation.SuppressLint
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.ui.component.Background
import com.harsh.walkie_talkie.ui.presentation.home.model.User
import com.harsh.walkie_talkie.ui.presentation.home.viewmodel.UserViewModel
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import com.harsh.walkie_talkie.ui.theme.WHITE

@SuppressLint("UnrememberedMutableInteractionSource")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundContactSheet(
    user: User,
    userViewModel: UserViewModel,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf(TextFieldValue()) }
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier.height(400.dp),
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
                        "Write a nickname for this contact.",
                        color = WHITE,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_bold)),
                    )
                    Text(
                        "Type something in below field, it will help you to identify contact easily:\n" +
                                "• UID: ${user.uid}.\n",
                        color = WHITE,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = WHITE,
                            unfocusedBorderColor = WHITE.copy(alpha = 0.2f),
                            focusedTextColor = WHITE,
                            unfocusedTextColor = WHITE,
                        ),
                        label = {
                            Text(
                                "Nickname",
                                color = WHITE,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                            )
                        },
                        textStyle = TextStyle(
                            color = WHITE,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        ),
                        maxLines = 1,
                        singleLine = true,
                    )
                }
                TextButton(
                    onClick = {
                        userViewModel.addUser(uid = user.uid, name = name.text, user.token.toString())
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
                        "Save Contact",
                        color = WHITE,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_bold)),
                    )
                }
            }
        }
    }
}