package com.rollinup.rollinup.component.chip

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.model.Severity
import com.rollinup.rollinup.component.ripple.CustomRipple
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.tooltip.ToolTip
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun Chip(
    text: String,
    severity: Severity = Severity.PRIMARY,
    textStyle: TextStyle = Style.chipContent,
    leadingIcon: DrawableResource? = null,
    trailingIcon: DrawableResource? = null,
    onClickLeadIcon: () -> Unit = {},
    onClickTrailIcon: () -> Unit = {},
    toolTipContent: String? = null,
) {
    Chip(
        text = text,
        severity = severity,
        textStyle = textStyle,
        leadingContent = leadingIcon?.let {
            @Composable
            {
                CustomRipple(
                    Color.Transparent
                ) {
                    Icon(
                        painter = painterResource(it),
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(
                                    color = Color.Unspecified,
                                    bounded = false,
                                )
                            ) {
                                onClickLeadIcon()
                            }
                    )
                }
            }
        },
        trailingContent = trailingIcon?.let {
            @Composable
            {
                Icon(
                    painter = painterResource(it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(
                                color = Color.Unspecified,
                                bounded = false,
                            )
                        ) {
                            onClickTrailIcon()
                        }
                )
            }
        },
        toolTipContent = toolTipContent
    )
}

@Composable
fun Chip(
    text: String,
    severity: Severity = Severity.PRIMARY,
    textStyle: TextStyle = Style.chipContent,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    toolTipContent: String? = null,
) {
    val chipColor = generateChipColor(severity)
    val interactionSource = remember { MutableInteractionSource() }

    ToolTip(
        toolTipContent ?: ""
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                ) {}
                .background(
                    shape = RoundedCornerShape(50),
                    color = chipColor.containerColor
                )
                .padding(
                    horizontal = itemGap8,
                    vertical = itemGap4
                )
                .hoverable(
                    interactionSource = interactionSource,
                    enabled = true,
                )

        ) {
            leadingContent?.invoke()
            Text(
                text = text,
                style = textStyle,
                color = chipColor.contentColor
            )
            trailingContent?.invoke()
        }

    }

}

@Composable
private fun generateChipColor(
    severity: Severity,
): ChipColor {
    val containerColor: Color
    val contentColor: Color
    when (severity) {
        Severity.DANGER -> {
            containerColor = theme.chipDangerBg
            contentColor = theme.chipDangerContent
        }

        Severity.PRIMARY -> {
            containerColor = theme.chipPrimaryBg
            contentColor = theme.chipPrimaryContent
        }

        Severity.SECONDARY -> {
            containerColor = theme.chipSecondaryBg
            contentColor = theme.chipSecondaryContent

        }

        Severity.WARNING -> {
            containerColor = theme.chipWarningBg
            contentColor = theme.chipWarningContent

        }

        Severity.SUCCESS -> {
            containerColor = theme.chipSuccessBg
            contentColor = theme.chipSuccessContent

        }

        Severity.DISABLED -> {
            containerColor = theme.chipDisabledBg
            contentColor = theme.chipDisabledContent

        }
    }

    return ChipColor(
        contentColor = contentColor,
        containerColor = containerColor
    )
}


internal data class ChipColor(
    val contentColor: Color,
    val containerColor: Color,
)