package com.rollinup.rollinup.component.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.checkbox.CheckBox
import com.rollinup.rollinup.component.ripple.CustomRipple
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.isCompact
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * A styled wrapper around the Material 3 [DropdownMenu].
 *
 * @param isShowDropDown Controls whether the menu is currently expanded.
 * @param onDismissRequest Callback invoked when the user requests to dismiss the menu.
 * @param modifier Modifier applied to the menu layout.
 * @param content The content of the menu, typically [DropDownMenuItem]s.
 */
@Composable
fun DropDownMenu(
    isShowDropDown: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    DropdownMenu(
        modifier = modifier,
        expanded = isShowDropDown,
        onDismissRequest = { onDismissRequest(false) },
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp,
        containerColor = theme.popUpBg,
    ) {
        content()
    }
}

/**
 * State holder for managing dropdown selections.
 *
 * @param T The type of items in the dropdown.
 * @property selectedItem The currently selected items.
 * @property expanded Whether the dropdown is expanded.
 * @property onDismissRequest Callback to close the dropdown.
 */
data class DropDownState<T>(
    val selectedItem: List<T>,
    val expanded: Boolean,
    val onDismissRequest: (Boolean) -> Unit,
)

/**
 * A dropdown menu item specifically designed for multi-select scenarios.
 *
 * Includes a checkbox to indicate selection state.
 *
 * @param label The text to display.
 * @param textColor The color of the label text.
 * @param isSelected Whether the item is currently selected (checked).
 * @param onClick Callback triggered when the item is clicked.
 */
@Composable
fun DropDownMenuMultiSelectItem(
    label: String,
    textColor: Color = theme.bodyText,
    isSelected: Boolean,
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
                onCheckedChange = { onClick() },
            )
            Text(
                text = label,
                style = Style.popupBody,
                color = textColor
            )
        }
    }
}

/**
 * A standard item for a [DropDownMenu].
 *
 * Supports an optional trailing icon.
 *
 * @param label The text to display.
 * @param icon Optional trailing icon resource.
 * @param textColor The color of the label text.
 * @param iconTint The tint color for the icon.
 * @param backgroundColor Background color for the item row.
 * @param onClick Callback triggered when the item is clicked.
 */
@Composable
fun DropDownMenuItem(
    label: String,
    icon: DrawableResource? = null,
    textColor: Color = theme.bodyText,
    iconTint: Color = theme.primary,
    backgroundColor: Color = Color.Transparent,
    onClick: () -> Unit,
) {
    val gap = if (isCompact) 8.dp else 12.dp

    CustomRipple {
        Row(
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .background(color = backgroundColor)
                .padding(
                    vertical = itemGap8,
                    horizontal = 16.dp
                )
                .width(150.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = Style.body,
                color = textColor
            )
            icon?.let {
                Spacer(gap)
                Icon(
                    painter = painterResource(it),
                    tint = iconTint,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}