package com.rollinup.rollinup.component.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.ripple.CustomRipple
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.isCompact
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ActionButton(
    label: String,
    icon: DrawableResource? = null,
    textColor: Color = theme.bodyText,
    iconTint: Color = theme.primary,
    onClick: () -> Unit,
) {
    val gap = if (isCompact) 12.dp else 16.dp

    CustomRipple {
        Row(
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .padding(
                    vertical = itemGap8,
                    horizontal = 16.dp
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(it),
                    tint = iconTint,
                    contentDescription = null
                )
                Spacer(gap)
            }
            Text(
                text = label,
                style = Style.popupBody,
                color = textColor
            )
        }
    }
}