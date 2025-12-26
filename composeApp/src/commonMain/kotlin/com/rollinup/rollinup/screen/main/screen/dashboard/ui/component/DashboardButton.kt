package com.rollinup.rollinup.screen.dashboard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.isCompact
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun DashBoardButton(
    text: String,
    icon: DrawableResource,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
) {
    val size = if (isCompact) Pair(162.dp, 42.dp) else Pair(174.dp, 48.dp)

    val baseColor: Color
    val iconBackgroundColor: Color
    val contentColor: Color

    if (isEnabled) {
        baseColor = theme.secondary
        iconBackgroundColor = theme.primary
        contentColor = theme.secondary
    } else {
        baseColor = theme.textFieldBackGround
        iconBackgroundColor = theme.textFieldStrokeDisabled
        contentColor = theme.textFieldBackGround
    }

    val buttonColor = ButtonDefaults.buttonColors().copy(
        containerColor = baseColor,
        contentColor = contentColor,
        disabledContainerColor = baseColor,
        disabledContentColor = contentColor
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .size(size.first, size.second),
        enabled = isEnabled,
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(
            topStart = 12.dp,
            bottomStart = 12.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp
        ),
        colors = buttonColor,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = baseColor),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .size(size.second)
                            .background(
                                color = iconBackgroundColor,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(itemGap4)
                                .fillMaxSize(),
                            painter = painterResource(icon),
                            tint = contentColor,
                            contentDescription = null
                        )
                    }
                    Spacer(itemGap8)
                    Text(
                        text = text,
                        color = iconBackgroundColor,
                        style = Style.label
                    )
                }
            }
        }
    )
}