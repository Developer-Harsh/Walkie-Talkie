package com.harsh.walkie_talkie.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.walkie_talkie.R
import com.harsh.walkie_talkie.ui.theme.GRADIENT_ONE
import com.harsh.walkie_talkie.ui.theme.GRADIENT_TWO
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import com.harsh.walkie_talkie.ui.theme.WHITE
import kotlinx.coroutines.launch

@Composable
fun SwipeToStartButton(
    modifier: Modifier = Modifier,
    onSwipeComplete: () -> Unit
) {
    val swipeWidth = 300.dp
    val thumbSize = 55.dp
    val cornerRadius = 40.dp

    val density = LocalDensity.current
    val maxOffsetPx = with(density) { swipeWidth.toPx() - thumbSize.toPx() - 60 }

    val animatableOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val swipeProgress = with(density) { (animatableOffset.value / (swipeWidth.toPx() - thumbSize.toPx())) }

    val animatedAlpha by animateFloatAsState(
        targetValue = 1f - swipeProgress,
        animationSpec = tween(durationMillis = 100), label = "textAlpha"
    )

    LaunchedEffect(Unit) {
        animatableOffset.updateBounds(0f, maxOffsetPx)
    }

    Box(
        modifier = modifier
            .width(swipeWidth)
            .height(thumbSize + 20.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .drawBehind {
                drawCircle(
                    color = LOW_OPACITY_WHITE,
                    radius = (thumbSize.toPx() + 150f) / 2f,
                    center = Offset(animatableOffset.value + thumbSize.toPx() / 1.45f, size.height / 2),
                    alpha = 0.6f
                )
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        scope.launch {
                            if (animatableOffset.value > maxOffsetPx * 0.9f) {
                                animatableOffset.animateTo(maxOffsetPx, tween(300))
                                onSwipeComplete()
                                animatableOffset.animateTo(0f, tween(300)) // Reset
                            } else {
                                animatableOffset.animateTo(0f, tween(300)) // Reset if not fully swiped
                            }
                        }
                    }
                ) { change, dragAmount ->
                    change.consume()
                    scope.launch {
                        animatableOffset.snapTo(
                            (animatableOffset.value + dragAmount).coerceIn(0f, maxOffsetPx)
                        )
                    }
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LOW_OPACITY_WHITE)
                .blur(24.dp)
        )

        Text(
            text = "Get Started >",
            fontSize = 14.sp,
            color = WHITE.copy(alpha = animatedAlpha),
            fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
            modifier = Modifier
                .padding(end = 20.dp)
                .align(Alignment.Center)
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(animatableOffset.value.toInt() + 30, 0) }
                .size(thumbSize)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(GRADIENT_ONE, GRADIENT_TWO))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.double_right),
                contentDescription = "Swipe Icon",
                tint = WHITE,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}
