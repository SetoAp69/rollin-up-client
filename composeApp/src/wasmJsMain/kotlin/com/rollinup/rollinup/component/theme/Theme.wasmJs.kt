package com.rollinup.rollinup.component.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
actual fun getColorScheme(): ColorScheme {
    val lightColorScheme = lightColorScheme(
        primary = primary,
        onPrimary = Color.White,
        primaryContainer = primaryContainer,
        onPrimaryContainer = primary,
        secondary = secondary,
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFF9EAFF),
        onSecondaryContainer = Color(0xFF965FD4),
        background = Color.Transparent,
        onBackground = Color(0xFFF9EAFF),
        surface = Color.Transparent,
        onSurface = Color(0xFFFFFFFF),
        error = danger,
        onError = Color(0xFFE4686A)
    )

    val darkColorScheme = darkColorScheme(
        primary = Color(0xFF704DC8),
        onPrimary = Color.White,
        primaryContainer = Color(0xFF6527AC),
        onPrimaryContainer = Color(0xFFDAC2E4),
        secondary,
        onSecondary = Color.White,
        secondaryContainer = Color(0xFF34244A),
        onSecondaryContainer = Color(0xFFBD9BD6),
        background = Color(0xFF34244A),
        onBackground = Color.White,
        surface = Color.Transparent,
        onSurface = Color(0xFFFFFFFF),
        error = danger,
        onError = Color(0xFFE4686A)
    )

    val colorScheme = if (isDark) darkColorScheme else lightColorScheme

    return colorScheme
}