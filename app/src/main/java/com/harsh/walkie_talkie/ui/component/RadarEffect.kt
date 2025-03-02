package com.harsh.walkie_talkie.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieConstants
import kotlinx.coroutines.delay

@Composable
fun RadarEffect(composition: LottieComposition?) {
    val fadeAnim = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3500)
            fadeAnim.animateTo(0f, tween(500)) // Fade out over 1 second
            delay(700)
            fadeAnim.animateTo(1f, tween(1000)) // Fade in over 1 second
        }
    }

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.graphicsLayer { alpha = fadeAnim.value }
    )
}
