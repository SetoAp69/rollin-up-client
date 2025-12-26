package com.rollinup.rollinup.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.theme.theme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun IconButton(
    icon: DrawableResource,
    size: Dp = 28.dp,
    severity: Severity = Severity.PRIMARY,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val color = getColor(severity)

    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(4.dp))
            .clickable(enabled) {
                onClick()
            }
            .background(
                color = if (enabled) color.containerColor else color.disabledContainerColor,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            tint = color.contentColor,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun getColor(
    severity: Severity,
): IconButtonColors {
    val containerColor: Color
    val contentColor: Color

    when (severity) {
        Severity.DANGER -> {
            contentColor = theme.textBtnPrimary
            containerColor = theme.danger
        }

        Severity.PRIMARY -> {
            contentColor = theme.textBtnPrimary
            containerColor = theme.primary
        }

        Severity.SECONDARY -> {
            contentColor = theme.textBtnPrimary
            containerColor = theme.secondary
        }

        Severity.WARNING -> {
            contentColor = theme.textBtnPrimary
            containerColor = theme.warning
        }

        Severity.SUCCESS -> {
            contentColor = theme.textBtnPrimary
            containerColor = theme.success
        }

        Severity.DISABLED -> {
            contentColor = theme.chipDisabledBg
            containerColor = theme.lineStroke
        }
    }

    return IconButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = theme.lineStroke,
        disabledContentColor = theme.chipDisabledBg
    )
}