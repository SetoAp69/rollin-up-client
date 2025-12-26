package com.rollinup.rollinup.component.loading

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.theme.theme


@Composable
fun ShimmerEffect(
    width: Dp = 60.dp,
    height: Dp = 14.dp,
    cornerRad: Dp = 8.dp,
    durationMillis: Int = 500,
    modifier: Modifier = Modifier,
) {
    ShimmerEffect(
        modifier = Modifier
            .clip(RoundedCornerShape(cornerRad))
            .width(width)
            .height(height)
            .then(modifier),
        durationMillis = durationMillis
    )
}

@Composable
fun ShimmerEffect(
    modifier: Modifier,
    durationMillis: Int = 500,
) {

    val transition = rememberInfiniteTransition(label = "shimmerColor")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerColor"
    )


    val brush = SolidColor(
        value = theme.textFieldBackGround.copy(alpha = (translateAnimation.value / 1000) + 0.2f)
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .then(modifier),
    ) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(brush)
        )
    }

}