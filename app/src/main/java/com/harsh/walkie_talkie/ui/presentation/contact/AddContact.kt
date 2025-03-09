package com.harsh.walkie_talkie.ui.presentation.contact

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.network.ApiService
import com.harsh.walkie_talkie.ui.component.Background
import com.harsh.walkie_talkie.ui.component.KeypadButton
import com.harsh.walkie_talkie.ui.presentation.home.model.User
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import com.harsh.walkie_talkie.ui.theme.WHITE
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactSheet(
    onDismiss: (User, Boolean) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var id by remember { mutableStateOf(TextFieldValue()) }
    val keypad = arrayListOf(7, 8, 9, 4, 5, 6, 1, 2, 3, 0, 0, 0)
    var user: User
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier.height(700.dp),
        containerColor = Color.Transparent,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        onDismissRequest = {},
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
                Text(
                    id.text,
                    color = WHITE,
                    fontSize = 28.sp,
                    fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_extrabold)),
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .height(30.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 40.dp)
                        .padding(bottom = 5.dp, top = 40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "You can add contacts:",
                        color = WHITE,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = {
                            if (id.text.isEmpty())
                                Toast.makeText(
                                    context,
                                    "Use keypad to dial ID.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            else if (id.text.length <= 11)
                                Toast.makeText(
                                    context,
                                    "Please enter a valid ID.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            else
                                ApiService().getFirebase(id.text, context) { profile, message ->
                                    if (message == "Not Found")
                                        Toast.makeText(
                                            context,
                                            "Profile not found!!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    else {
                                        user = User(
                                            name = "",
                                            uid = profile!!.uid,
                                            token = profile.token
                                        )
                                        onDismiss(user, true)
                                    }
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
                            "Add Contact!",
                            color = WHITE,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans_bold)),
                        )
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(horizontal = 45.dp),
                    horizontalArrangement = Arrangement.spacedBy(30.dp),
                    verticalArrangement = Arrangement.spacedBy(30.dp),
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    items(keypad.size) {
                        KeypadButton(
                            index = it,
                            text = keypad[it].toString(),
                            onClick = {
                                if (it == 11)
                                    id = TextFieldValue(id.text.dropLast(1))
                                else if (id.text.length <= 11)
                                    id =
                                        TextFieldValue("${id.text}${separator(id.text)}${keypad[it]}")
                            }
                        )
                    }
                }
            }
        }
    }
}

fun separator(text: String): String {
    return if (text.length == 3 || text.length == 8) "-" else ""
}