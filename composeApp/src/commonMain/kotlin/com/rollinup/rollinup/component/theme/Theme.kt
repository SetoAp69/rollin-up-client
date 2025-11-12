package com.rollinup.rollinup.component.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


class Theme(
    val isDark: Boolean = false,
) {
    val primary get() = if (isDark) Color(0xFF704DC8) else Color(0xFF965FD4)
    val success get() = if (isDark) Color(0xFF7BE824) else Color(0xFF80D838)
    val success50 get() = if (isDark) Color(0xFF154C10) else Color(0xFFC0FF8D)
    val warning get() = if (isDark) Color(0xFFFBD128) else Color(0xFFFDB128)
    val danger get() = if (isDark) Color(0xFFB20434) else Color(0xFFE4686A)
    val danger50 get() = if (isDark) Color(0xFFEB527C) else Color(0xFFED8789)
    val secondary get() = if (isDark) Color(0xFF34244A) else Color(0xFFF9EAFF)
    val secondary50 get() = if (isDark) Color(0xFFD7B4FF) else Color(0xFF965FD4)
    val textFieldBackGround get() = if (isDark) Color(0xFF2F1F2E) else Color(0xFFE9E9E9)
    val textFieldBgError get() = if (isDark) Color(0xFF633743) else Color(0xFFFFE9E9)
    val textFieldBackgroundError get() = if (isDark) Color(0xFF633743) else Color(0xFFFFE9E9)
    val textFieldStroke get() = if (isDark) Color(0xFF363434) else Color(0x908E93)
    val textFieldBgDisabled get() = if (isDark) Color(0xFF1D181D) else Color(0xFFF6F6F6)
    val textFieldStrokeDisabled get() = if (isDark) Color(0xFF715E70) else Color(0xFF8E8C8C)
    val textFieldPlaceHolder get() = if (isDark) Color(0xFF5C3C5A) else Color(0xFFC5C3C3)
    val lineStroke get() = if (isDark) Color(0xFF896BA9) else Color(0xFF9A80B8)
    val popUpStroke get() = if (isDark) Color(0xFF3C2545) else Color(0xFFF9EAFF)
    val popUpBgSelected get() = if (isDark) Color(0xFF492B55) else Color(0xFFFAEFFF)
    val popUpBg get() = if (isDark) Color(0xFF150E1D) else Color(0xFFFFFFFF)
    val background get() = if (isDark) Color(0xFF34244A) else Color(0xFFF9EAFF)

    // Chips
    val chipPrimaryBg get() = if (isDark) Color(0xFF6527AC) else Color(0xFF965FD4)
    val chipPrimaryContent get() = if (isDark) Color(0xFFDAC2E4) else Color(0xFFF9EAFF)
    val chipSecondaryBg get() = if (isDark) Color(0xFF34244A) else Color(0xFFF9EAFF)
    val chipSecondaryContent get() = if (isDark) Color(0xFFBD9BD6) else Color(0xFF965FD4)
    val chipSuccessBg get() = if (isDark) Color(0xFF95DB5C) else Color(0xFFCCFFA3)
    val chipSuccessContent get() = if (isDark) Color(0xFF306604) else Color(0xFF316C02)
    val chipWarningBg get() = if (isDark) Color(0xFFF0AD72) else Color(0xFFFFD4AF)
    val chipWarningContent get() = if (isDark) Color(0xFFBF6600) else Color(0xFFDD7600)
    val chipDangerContent get() = Color(0xFFFFE9E9)
    val chipDangerBg get() = if (isDark) Color(0xFFB20434) else Color(0xFFE4686A)
    val chipDisabledBg get() = if (isDark) Color(0xFF1F1F1F) else Color(0xFF1F1F1F)

    val btnDisabled get() = if (isDark) Color(0xFF271B34) else Color(0xFF908E93)
    val btnDisabledContent get() = if (isDark) Color(0xFF372445) else Color(0xFFC5C3C3)

    // Text
    val bodyText get() = if (isDark) Color(0xFFFFFFFF) else Color(0xFF252421)
    val textFieldText get() = if (isDark) Color(0xFF715E70) else Color(0xFF8E8C8C)
    val textPrimary get() = if (isDark) Color(0xFFFFFFFF) else Color(0xFF704DC8)
    val textSuccess get() = if (isDark) Color(0xFF85B162) else Color(0xFF128312)
    val textError get() = if (isDark) Color(0xFFEE1E28) else Color(0xFFE4686A)

    // --- Properties with one value (same for light/dark) ---
    val chipDisabledContent get() = Color(0xFF5C5757)
    val textSubtitle get() = Color(0xFF6C6870)
    val textBtnPrimary get() = Color(0xFFFFFFFF)
    val textBtnSecondary get() = Color(0xFF8B6FAC)


    val shadow get() = if (isDark) Color(0xFF703887)/*.copy(alpha = 0.75f)*/ else Color(0xFFF9EAFF)/*.copy(alpha = 0.75f)*/
}

val isDark @Composable get() = isSystemInDarkTheme()

val theme @Composable get() = LocalTheme.current

@Composable
fun RollinUpTheme(
    content: @Composable () -> Unit,
) {
    val colorScheme = getColorScheme()

    MaterialTheme(
        colorScheme = colorScheme,
        typography = rollinUpTypography(),
        content = content
    )
}

@Composable
expect fun getColorScheme(): ColorScheme

