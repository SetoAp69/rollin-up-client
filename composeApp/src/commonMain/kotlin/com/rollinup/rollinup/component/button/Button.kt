package com.rollinup.rollinup.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.isCompact
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun Button(
    text: String? = null,
    severity: Severity = Severity.PRIMARY,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.FILLED,
    height: Dp = ButtonDefault.height,
    enabled: Boolean = true,
    cornerRad: Dp? = null,
    contentPadding: PaddingValues? = null,
    buttonColors: ButtonColors? = null,
    leadingIcon: DrawableResource? = null,
    trailingIcon: DrawableResource? = null,
    onClick: () -> Unit,
) {
    val buttonColor = buttonColors ?: getButtonColor(severity, type)
    val borderWidth = 2.5.dp
    val borderColor = if (enabled) buttonColor.contentColor else theme.btnDisabled

    val border = when (type) {
        ButtonType.OUTLINED -> BorderStroke(
            width = borderWidth,
            brush = SolidColor(borderColor)
        )

        else -> null
    }

    val cornerRad = cornerRad ?: if (isCompact) 12.dp else 16.dp
    val shape = RoundedCornerShape(cornerRad)
    val contentGap = if (isCompact) 8.dp else 12.dp

    val text = if (type != ButtonType.ICON) text else null

    ButtonBasic(
        modifier = modifier
            .height(height),
        onClick = onClick,
        enabled = enabled,
        buttonColors = buttonColor,
        border = border,
        shape = shape,
        contentPadding = contentPadding,
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                leadingIcon?.let {
                    ButtonIcon(
                        icon = it,
                    )
                }
                Spacer(contentGap)
                text?.let {
                    Text(
                        text = it,
                        style = ButtonDefault.textStyle,
                    )
                }
                Spacer(contentGap)
                trailingIcon?.let {
                    ButtonIcon(
                        icon = it,
                    )
                }
            }
        },
    )
}

@Composable
internal fun ButtonIcon(
    icon: DrawableResource,
) {
    Box(
        modifier = Modifier
            .size(28.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
        )
    }
}

@Composable
internal fun ButtonBasic(
    modifier: Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
    border: BorderStroke? = null,
    shape: Shape? = null,
    buttonColors: ButtonColors,
    contentPadding: PaddingValues? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val elevation = ButtonDefaults.buttonElevation()
    val shape = shape ?: ButtonDefault.shape
    val contentPadding = contentPadding ?: ButtonDefault.contentPadding
    val interactionSource = remember { MutableInteractionSource() }

    Button(
        onClick = {
            focusManager.clearFocus()
            onClick()
        },
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = buttonColors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
internal fun getButtonColor(
    severity: Severity,
    buttonType: ButtonType,
): ButtonColors {

    val filledContainerColor = when (severity) {
        Severity.DANGER -> theme.danger
        Severity.PRIMARY -> theme.primary
        Severity.SECONDARY -> theme.secondary
        Severity.WARNING -> theme.warning
        Severity.SUCCESS -> theme.success
        Severity.DISABLED -> theme.lineStroke
    }

    val nonFilledContentColor = when (severity) {
        Severity.DANGER -> theme.danger
        Severity.PRIMARY -> theme.primary
        Severity.SECONDARY -> theme.secondary50
        Severity.WARNING -> theme.warning
        Severity.SUCCESS -> theme.success
        Severity.DISABLED -> theme.chipDisabledBg
    }

    val filledContentColor = when (severity) {
        Severity.DANGER -> theme.textBtnPrimary
        Severity.PRIMARY -> theme.textBtnPrimary
        Severity.SECONDARY -> theme.secondary50
        Severity.WARNING -> theme.textBtnPrimary
        Severity.SUCCESS -> theme.textBtnPrimary
        Severity.DISABLED -> theme.lineStroke
    }

    val contentColor = when (buttonType) {
        ButtonType.FILLED -> filledContentColor
        else -> nonFilledContentColor
    }

    val disabledContainerColor = when (buttonType) {
        ButtonType.FILLED -> theme.btnDisabled
        else -> Color.Transparent
    }

    val disabledContentColors = when (buttonType) {
        ButtonType.FILLED -> theme.btnDisabledContent
        else -> theme.btnDisabled
    }

    val containerColor = when (buttonType) {
        ButtonType.FILLED -> filledContainerColor
        else -> Color.Transparent
    }


    val buttonColors = ButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColors
    )

    return buttonColors
}

enum class ButtonType {
    FILLED, OUTLINED, ICON
}

object ButtonDefault {
    val contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
    val shape = RoundedCornerShape(12.dp)
    val height
        @Composable get() = 42.dp

    val textStyle
        @Composable get() = Style.popupTitle
}