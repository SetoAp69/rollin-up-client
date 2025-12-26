package com.rollinup.rollinup.component.ripple

import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.rollinup.rollinup.component.theme.theme

@Composable
fun CustomRipple(
    color: Color = theme.primary,
    content: @Composable () -> Unit,
) {
    val rippleConfiguration = RippleConfiguration(
        color = color,
    )

    CompositionLocalProvider(
        LocalRippleConfiguration provides rippleConfiguration
    ) {
        content()
    }
}