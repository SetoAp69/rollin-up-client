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
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.theme.theme
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_more_fill_24


@Composable
fun Card(
    shadowColor: Color = theme.primary,
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
                    elevation = 4.dp,
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
                    .padding(screenPadding)
            ) {
                content()
            }
        }
    }
}

@Composable
fun Card(
    shadowColor: Color = theme.primary,
    lineStroke: Color? = theme.popUpStroke,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
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
                        )
                        ,
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