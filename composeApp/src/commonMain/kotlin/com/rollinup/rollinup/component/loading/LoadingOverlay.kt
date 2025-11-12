package com.rollinup.rollinup.component.loading

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme

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
                    .background(Color(0xFF150E1D).copy(alpha = 0.5F))
                    .fillMaxSize(),
                contentAlignment = Alignment.Center

            ) {
                val animationState = remember { Animatable(0F) }
                Text(
                    text = "LOADING WAK",
                    style = Style.header,
                    color = theme.textBtnPrimary
                )
            }
        }
    }
}
