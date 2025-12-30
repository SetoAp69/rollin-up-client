package com.rollinup.rollinup.component.radio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_radio_selected_line_24
import rollin_up.composeapp.generated.resources.ic_radio_unselect_line_24

/**
 * A group of radio buttons arranged in a flowing row layout.
 *
 * Useful for selecting a single option from a list where the items might wrap
 * to the next line if horizontal space is constrained. Wrapped in a [TextFieldTitle].
 *
 * @param T The type of value associated with the options.
 * @param modifier Modifier applied to the flow row container.
 * @param value The currently selected value.
 * @param onValueChange Callback invoked when a new option is selected.
 * @param options List of available options [OptionData].
 * @param title Optional title displayed above the radio group.
 * @param horizontalArrangement Arrangement of items horizontally.
 * @param verticalArrangement Arrangement of items vertically (when wrapped).
 * @param itemVerticalAlignment Vertical alignment of items within their row.
 * @param maxItemsInEachRow Maximum number of items before forcing a wrap.
 * @param maxLines Maximum number of lines to display.
 */
@Composable
fun <T> RadioSelectorRow(
    modifier: Modifier = Modifier,
    value: T? = null,
    onValueChange: (T) -> Unit,
    options: List<OptionData<T>>,
    title: String = "",
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(itemGap4),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(itemGap4),
    itemVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
) {
    TextFieldTitle(
        title = title
    ) {
        FlowRow(
            modifier = modifier,
            horizontalArrangement = horizontalArrangement,
            verticalArrangement = verticalArrangement,
            itemVerticalAlignment = itemVerticalAlignment,
            maxItemsInEachRow = maxItemsInEachRow,
            maxLines = maxLines
        ) {
            options.fastForEach {
                RadioButton(
                    option = it,
                    isSelected = it.value == value,
                    onSelect = onValueChange
                )
            }
        }
    }
}


/**
 * A single radio button item with a label.
 *
 * @param T The type of value associated with the option.
 * @param option The data for this option (label and value).
 * @param isSelected Whether this option is currently selected.
 * @param onSelect Callback invoked when this option is clicked.
 */
@Composable
fun <T> RadioButton(
    option: OptionData<T>,
    isSelected: Boolean,
    onSelect: (T) -> Unit,
) {
    val icon =
        if (isSelected)
            Res.drawable.ic_radio_selected_line_24
        else
            Res.drawable.ic_radio_unselect_line_24

    Row(
        modifier = Modifier
            .clickable {
                onSelect(option.value)
            }
            .padding(itemGap4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = theme.primary
        )
        Text(
            text = option.label,
            color = theme.bodyText,
            style = Style.body
        )
    }
}