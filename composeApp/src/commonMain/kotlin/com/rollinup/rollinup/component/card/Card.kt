package com.rollinup.rollinup.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.ripple.CustomRipple
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.theme.theme
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_more_fill_24

/**
 * A basic, customizable Card container with shadow, border, and click handling.
 *
 * This component provides the visual foundation for cards in the application,
 * including elevation, rounded corners, and ripple effects.
 *
 * @param shadowColor The color of the card's shadow.
 * @param lineStroke The color of the card's border stroke. Passing null removes the border.
 * @param backgroundColor The background color of the card.
 * @param onClick Callback triggered when the card is clicked.
 * @param onLongClick Callback triggered when the card is long-pressed.
 * @param content The content to display inside the card.
 */
@Composable
fun Card(
    shadowColor: Color = theme.shadow,
    lineStroke: Color? = theme.popUpStroke,
    backgroundColor: Color = theme.popUpBg,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    CustomRipple {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    ambientColor = shadowColor,
                    spotColor = shadowColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    brush = SolidColor(
                        value = lineStroke ?: Color.Transparent
                    ),
                    width = 1.dp,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )

                .clip(
                    RoundedCornerShape(8.dp)
                )
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                )
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(screenPaddingValues)
            ) {
                content()
            }
        }
    }
}

/**
 * A specialized Card variant that includes a dedicated "More Actions" button.
 *
 * This version places the provided [content] on the left and a 3-dot menu icon
 * on the right. Useful for list items that require secondary actions.
 *
 * @param shadowColor The color of the card's shadow.
 * @param lineStroke The color of the card's border stroke.
 * @param onClick Callback triggered when the main body of the card is clicked.
 * @param onLongClick Callback triggered when the card is long-pressed.
 * @param showAction Controls the visibility of the action button.
 * @param onClickAction Callback triggered when the "More Actions" icon is clicked.
 * @param backgroundColor The background color of the card.
 * @param content The main content to display on the left side of the card.
 */
@Composable
fun Card(
    shadowColor: Color = theme.primary,
    lineStroke: Color? = theme.popUpStroke,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    showAction: Boolean,
    onClickAction: () -> Unit,
    backgroundColor: Color = theme.popUpBg,
    content: @Composable () -> Unit,
) {
    Card(
        shadowColor = shadowColor,
        lineStroke = lineStroke,
        onClick = onClick,
        onLongClick = onLongClick,
        backgroundColor = backgroundColor,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.weight(1f)) {
                content()
            }
            if (showAction) {
                Spacer(itemGap8)
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable {
                            onClickAction()
                        }
                        .background(
                            color = theme.popUpBgSelected,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_more_fill_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(2.dp)
                            .size(16.dp),
                        tint = theme.textBtnSecondary
                    )
                }
            }
        }
    }
}