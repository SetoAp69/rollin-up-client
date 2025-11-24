package com.rollinup.rollinup.component.dropdown

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.checkbox.CheckBox
import com.rollinup.rollinup.component.ripple.CustomRipple
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getScreenHeight
import com.rollinup.rollinup.component.utils.isCompact
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun DropDownMenu(
    isShowDropDown: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    DropdownMenu(

        expanded = isShowDropDown,
        onDismissRequest = { onDismissRequest(false) },
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp,
        containerColor = theme.popUpBg,
    ) {
        content()
    }
}

data class DropDownState<T>(
    val selectedItem:List<T>,
    val expanded:Boolean,
    val onDismissRequest: (Boolean) -> Unit,
)

@Composable
fun DropDownMenuMultiSelectItem(
    label: String,
    textColor: Color = theme.bodyText,
    isSelected:Boolean,
    onClick: () -> Unit,
) {
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
                .widthIn(max = 150.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CheckBox(
                modifier = Modifier.size(16.dp),
                checked = isSelected,
                onCheckedChange = {onClick()},
            )
            Text(
                text = label,
                style = Style.popupBody,
                color = textColor
            )
        }
    }
}

@Composable
fun DropDownMenuItem(
    label: String,
    icon: DrawableResource? = null,
    textColor: Color = theme.bodyText,
    iconTint: Color = theme.primary,
    onClick: () -> Unit,
) {
    val gap = if (isCompact) 8.dp else 12.dp

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
                .width( 150.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let {
                Icon(
                    painter = painterResource(it),
                    tint = iconTint,
                    contentDescription = null,
                    modifier   = Modifier.size(16.dp)
                )
                Spacer(gap)
            }
            Text(
                text = label,
                style = Style.body,
                color = textColor
            )
        }
    }
}



