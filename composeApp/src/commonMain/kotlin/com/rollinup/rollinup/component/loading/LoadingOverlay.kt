package com.rollinup.rollinup.component.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rollinup.rollinup.component.imageview.Image
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.DotLottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieAnimatable
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import rollin_up.composeapp.generated.resources.Res

/**
 * A full-screen overlay dialog that displays a loading animation.
 *
 * This component blocks interaction with the underlying UI while [show] is true.
 *
 * @param show Controls the visibility of the overlay.
 */
@Composable
fun LoadingOverlay(
    show: Boolean = false,
) {
    if (show) {
        Dialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            onDismissRequest = {}
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF150E1D).copy(alpha = 0.8F))
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation()
            }
        }
    }
}

/**
 * Displays the specific Lottie animation for the loading state (paper plane).
 */
@Composable
fun LottieAnimation() {
    val animatable = rememberLottieAnimatable()
    val lottieAnimation by rememberLottieComposition {
        LottieCompositionSpec.DotLottie(
            Res.readBytes("files/animation_paperplane_loading.lottie")
        )
    }
    val painter = rememberLottiePainter(
        composition = lottieAnimation,
        iterations = Compottie.IterateForever,
    )

    LaunchedEffect(Unit) {
        animatable.animate(
            composition = lottieAnimation,
            iteration = Compottie.IterateForever,
            iterations = Compottie.IterateForever,
            continueFromPreviousAnimate = true,
        )
    }

    Image(
        painter = painter,
        width = 400.dp,
        height = 400.dp
    )
}