package com.harsh.walkie_talkie.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.harsh.walkie_talkie.ui.theme.CIRCLES_WHITE
import com.harsh.walkie_talkie.ui.theme.LOW_OPACITY_WHITE
import kotlin.math.max

@Composable
fun RippleEffect(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()

    val rippleRadius by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val rippleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(300.dp)) {
            val center = Offset(size.width / 2, size.height / 2)

            for (i in 0..3) {
                drawIntoCanvas { canvas ->
                    val paint = Paint().asFrameworkPaint().apply {
                        isAntiAlias = true
                        color = LOW_OPACITY_WHITE.copy(alpha = max(0f, rippleAlpha - (i * 0.02f))).toArgb()
                        maskFilter = android.graphics.BlurMaskFilter(24f, android.graphics.BlurMaskFilter.Blur.OUTER)
                    }
                    canvas.drawCircle(
                        center = center,
                        radius = rippleRadius - (i * 130f),
                        paint = paint.asComposePaint()
                    )
                }
            }
        }
    }
}

@Composable
fun RippleBox(modifier: Modifier = Modifier, size: Dp? = 300.dp) {
    val infiniteTransition = rememberInfiniteTransition()

    val rippleRadius by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = 450f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val rippleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(size!!),
            contentAlignment = Alignment.Center
        ) {
            for (i in 0..3) {
                Box(
                    modifier = Modifier
                        .size((rippleRadius - (i * 110f)).dp)
                        .background(CIRCLES_WHITE.copy(alpha = 0.1f).copy(alpha = max(0f, rippleAlpha - (i * 0.0f))), shape = CircleShape)
                        .align(Alignment.Center)
                )
            }
        }
    }
}
