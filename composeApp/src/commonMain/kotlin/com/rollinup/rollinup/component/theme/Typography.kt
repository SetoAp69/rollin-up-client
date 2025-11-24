package com.rollinup.rollinup.component.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rollinup.rollinup.component.utils.isCompact
import org.jetbrains.compose.resources.Font
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.figtree_italic
import rollin_up.composeapp.generated.resources.figtree_regular

val fontFamily
    @Composable get() = FontFamily(
        Font(Res.font.figtree_regular),
        Font(Res.font.figtree_italic),
    )

@Composable
fun rollinUpTypography() = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}


object Style {
    val body
        @Composable get() = if (isCompact) generateStyle(
            12,
            FontWeight.W400
        ) else generateStyle(12, FontWeight.W400)

    val label
        @Composable get() = if(isCompact) generateStyle(
            14,
            FontWeight.W500
        ) else generateStyle(16, FontWeight.W500)

    val title
        @Composable get() = if (isCompact) generateStyle(
            12,
            FontWeight.W600
        ) else generateStyle(14, FontWeight.W600)

    val popupBody
        @Composable get() = if (isCompact) generateStyle(
            16,
            FontWeight.W400
        ) else generateStyle(18, FontWeight.W400)

    val popupTitle
        @Composable get() = if (isCompact) generateStyle(
            16,
            FontWeight.W600
        ) else generateStyle(18, FontWeight.W600)
    val chipContent
        @Composable get() = if (isCompact) generateStyle(
            10,
            FontWeight.W500
        ) else generateStyle(12, FontWeight.W500)
    val small
        @Composable get() = if (isCompact) generateStyle(
            10,
            FontWeight.W400
        ) else generateStyle(12, FontWeight.W400)
    val header
        @Composable get() = if (isCompact) generateStyle(
            20,
            FontWeight.W600
        ) else generateStyle(
            24, FontWeight.W600
        )

    val headerBold
        @Composable get() = generateStyle(24, FontWeight.W700)

    private fun generateStyle(fontSize: Int, weight: FontWeight): TextStyle {
        val height = when (fontSize) {
            in (0..15) -> 16
            in (16..24) -> 20
            else -> 24
        }

        return TextStyle(
            fontSize = fontSize.sp,
            fontWeight = weight,
            lineHeight = height.sp,
            letterSpacing = 0.5.sp,
        )
    }

}


