package com.rollinup.rollinup.component.empty

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.imageview.Image
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import io.github.alexzhirkevich.compottie.DotLottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieAnimatable
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_record_empty

/**
 * A placeholder component used when there is no data to display.
 *
 * It renders a Lottie animation (`animation_empty_data.lottie`) and a text message
 * indicating that the record list is empty.
 */
@Composable
fun EmptyRecord() {
    val lottieComposition by rememberLottieComposition {
        LottieCompositionSpec.DotLottie(
            Res.readBytes("files/animation_empty_data.lottie")
        )
    }
    val animatable = rememberLottieAnimatable()
    val painter = rememberLottiePainter(lottieComposition)

    LaunchedEffect(Unit) {
        animatable.animate(
            composition = lottieComposition,
            iteration = 3
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painter,
                width = 200.dp,
                height = 200.dp
            )
            Spacer(12.dp)
            Text(
                text = stringResource(Res.string.msg_record_empty),
                style = Style.popupBody,
                color = theme.textPrimary
            )

        }
    }
}