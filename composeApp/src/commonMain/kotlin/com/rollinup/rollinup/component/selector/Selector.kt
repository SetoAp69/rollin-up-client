package com.rollinup.rollinup.component.selector

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.ripple.CustomRipple
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.textfield.TextError
import com.rollinup.rollinup.component.textfield.TextFieldDefaults
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.isCompact
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_checkbox_blank_line_24
import rollin_up.composeapp.generated.resources.ic_checkbox_selected_fill_24
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_right_24
import rollin_up.composeapp.generated.resources.ic_radio_selected_line_24
import rollin_up.composeapp.generated.resources.ic_radio_unselect_line_24
import rollin_up.composeapp.generated.resources.label_apply
import rollin_up.composeapp.generated.resources.label_select
import rollin_up.composeapp.generated.resources.label_select_all

/**
 * A form field that opens a selector dialog/bottom sheet for picking a single value.
 *
 * It looks like a standard text field with a dropdown arrow.
 *
 * @param title Label for the field.
 * @param value The currently selected value.
 * @param options List of available [OptionData].
 * @param isError Shows error styling if true.
 * @param isEnabled Controls interaction.
 * @param textError Error message displayed below the field.
 * @param onValueChange Callback with the new selected value.
 * @param placeHolder Text displayed when no value is selected.
 */
@Composable
fun <T> SingleSelectorField(
    title: String,
    value: T?,
    options: List<OptionData<T>>,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    textError: String? = null,
    onValueChange: (T) -> Unit,
    placeHolder: String = stringResource(Res.string.label_select),
) {
    var showSelector by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(if (showSelector) 90F else 0F)
    val sValue = options.find { it.value == value }?.label ?: placeHolder
    val backgroundColor: Color
    val contentColor: Color

    if (isError) {
        backgroundColor = theme.textFieldBgError
        contentColor = theme.danger
    } else {
        backgroundColor = theme.secondary
        contentColor = theme.textPrimary
    }

    TextFieldTitle(
        title = title,
    ) {
        Row(
            modifier = Modifier
                .clickable(isEnabled) {
                    showSelector = true
                }
                .fillMaxWidth()
                .height(TextFieldDefaults.height)
                .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = itemGap4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(itemGap4),
        ) {
            if (isEnabled) {
                Icon(
                    painter = painterResource(Res.drawable.ic_drop_down_arrow_line_right_24),
                    tint = contentColor,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(rotationState),
                    contentDescription = null
                )
            }
            Text(
                text = sValue,
                style = Style.title,
                color = contentColor
            )
        }
        TextError(
            text = textError ?: "",
            isError = isError
        )
    }

    SingleSelector(
        isShowSelector = showSelector,
        onDismissRequest = { showSelector = it },
        title = title,
        value = value,
        options = options,
        onValueChange = onValueChange
    )


}

/**
 * A compact dropdown selector for multiple values, commonly used in filters.
 *
 * Displays a summary string (e.g., "3 selected") when multiple items are chosen.
 *
 * @param title Label for the selector.
 * @param isLoading Shows loading state in the dropdown content.
 * @param value List of currently selected values.
 * @param enable Controls interaction.
 * @param options List of available options.
 * @param width Fixed width for the selector field.
 * @param onValueChange Callback with the updated list of selected values.
 */
@Composable
fun <T> MultiDropDownSelector(
    title: String,
    isLoading: Boolean = false,
    value: List<T>,
    enable: Boolean = true,
    contentColor: Color = theme.textPrimary,
    backgroundColor: Color = theme.secondary,
    placeHolder: String = "-",
    options: List<OptionData<T>>,
    width: Dp? = 124.dp,
    onValueChange: (List<T>) -> Unit,
) {
    var showSelector by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (showSelector) 90f else 0F)

    val sValue = when {
        value.size == 1 -> options.find { it.value == value.first() }?.label?.take(10) ?: ""
        value.isEmpty() -> placeHolder
        else -> value.size.toString() + " selected"
    }

    val modifier = width?.let {
        Modifier.width(it)
    } ?: Modifier.fillMaxWidth()

    Box {
        TextFieldTitle(
            title = title,
        ) {
            Row(
                modifier = Modifier
                    .clickable(enable) {
                        showSelector = true
                    }
                    .then(modifier)
                    .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
                    .padding(itemGap4),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(itemGap4),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_drop_down_arrow_line_right_24),
                    tint = contentColor,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(rotationState),
                    contentDescription = null
                )
                Text(
                    text = sValue,
                    style = Style.title,
                    color = contentColor
                )
            }
        }

        MultiSelector(
            isShowSelector = showSelector,
            isLoading = isLoading,
            title = title,
            value = value,
            options = options,
            onValueChange = onValueChange,
            onDismissRequest = { showSelector = it }
        )
    }
}

/**
 * A compact dropdown selector for a single value, commonly used in filters or small forms.
 *
 * @param title Label for the selector.
 * @param value Currently selected value.
 * @param enable Controls interaction.
 * @param options List of available options.
 * @param onValueChange Callback with the new value.
 */
@Composable
fun <T> SingleDropDownSelector(
    title: String,
    value: T?,
    enable: Boolean = true,
    contentColor: Color = theme.textPrimary,
    backgroundColor: Color = theme.secondary,
    placeHolder: String = "-",
    isError: Boolean = false,
    width: Dp? = 100.dp,
    isLoading: Boolean = false,
    options: List<OptionData<T>>,
    onValueChange: (T) -> Unit,
) {
    val backgroundColor = if (isError) theme.textFieldBgError else backgroundColor
    val contentColor = if (isError) theme.danger else contentColor

    var showSelector by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (showSelector) 90f else 0F)

    val sValue = options.find {
        it.value == value
    }?.label ?: placeHolder

    val modifier = width?.let {
        Modifier.width(it)
    } ?: Modifier.fillMaxWidth()
    TextFieldTitle(
        title = title,
    ) {
        Row(
            modifier = Modifier
                .clickable(enable) {
                    showSelector = true
                }
                .then(modifier)
                .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
                .padding(itemGap4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(itemGap4),
        ) {
            if (enable) {
                Icon(
                    painter = painterResource(Res.drawable.ic_drop_down_arrow_line_right_24),
                    tint = contentColor,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(rotationState),
                    contentDescription = null
                )
            }
            Text(
                text = sValue,
                style = Style.title,
                color = contentColor
            )
        }

        SingleSelector(
            title = title,
            value = value,
            options = options,
            isLoading = isLoading,
            onValueChange = onValueChange,
            onDismissRequest = { showSelector = it },
            isShowSelector = showSelector,
        )
    }
}

/**
 * Platform-specific implementation for showing the single selection dialog/sheet.
 */
@Composable
expect fun <T> SingleSelector(
    isShowSelector: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    title: String,
    value: T?,
    options: List<OptionData<T>>,
    isLoading: Boolean = false,
    onValueChange: (T) -> Unit,
)

/**
 * The content composable for a single selector list.
 *
 * Renders a list of radio-button style items.
 */
@Composable
fun <T> SingleSelectorContent(
    title: String,
    isDropDown: Boolean = false,
    value: T?,
    options: List<OptionData<T>>,
    onValueChange: (T) -> Unit,
    onDismissRequest: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (isCompact) 12.dp else 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(
                start = screenPadding,
                end = screenPadding,
                bottom = itemGap8
            ),
            text = title,
            style = Style.popupTitle,
            color = theme.bodyText
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(itemGap4)
        ) {
            items(options) { option ->
                SelectorItem(
                    isDropDown = isDropDown,
                    isSingle = true,
                    isSelected = option.value == value,
                    optionData = option,
                    onClick = { value ->
                        onValueChange(value)
                        onDismissRequest(false)
                    }
                )
            }
        }
    }
}

/**
 * Platform-specific implementation for showing the multi-selection dialog/sheet.
 */
@Composable
expect fun <T> MultiSelector(
    isShowSelector: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    title: String,
    value: List<T>,
    options: List<OptionData<T>>,
    isLoading: Boolean = false,
    onValueChange: (List<T>) -> Unit,
)

/**
 * The content composable for a multi-selector list.
 *
 * Renders a list of checkbox style items with a "Select All" option and an Apply button.
 */
@Composable
fun <T> MultiSelectorContent(
    title: String,
    value: List<T>,
    options: List<OptionData<T>>,
    onValueChange: (List<T>) -> Unit,
    isDropDown: Boolean = false,
) {
    var tempValue by remember { mutableStateOf(value) }

    LaunchedEffect(value) {
        tempValue = value
    }

    val isAllSelected = tempValue.size == options.size
    val itemGap = if (isDropDown) 0.dp else itemGap4
    val titleStyle = if (isDropDown) Style.title else Style.popupTitle


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = if (isCompact) 12.dp else 16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(
                    start = screenPadding,
                    end = screenPadding,
                    bottom = itemGap8
                ),
                text = title,
                style = titleStyle,
                color = theme.bodyText
            )
            SelectAll(
                isAllSelected = isAllSelected,
                onClick = { selectAll ->
                    tempValue = if (selectAll) {
                        options.map { it.value }
                    } else {
                        emptyList()
                    }
                },
                isDropDown = isDropDown
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(itemGap)
            ) {
                items(options) { option ->
                    SelectorItem(
                        isSingle = false,
                        isDropDown = isDropDown,
                        isSelected = option.value in tempValue,
                        optionData = option,
                        onClick = { value ->
                            if (value in tempValue) {
                                tempValue = tempValue.filter { it != value }
                            } else {
                                tempValue = tempValue.plus(value)
                            }
                        }
                    )
                }
            }
        }
        Spacer(itemGap4)
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isCompact) 12.dp else 16.dp),
            text = stringResource(Res.string.label_apply)
        ) {
            onValueChange(tempValue)
        }
    }
}

/**
 * Placeholder showing shimmer effect while options are loading.
 */
@Composable
fun SelectorLoading(
    title: String,
    isDropDown: Boolean,
) {
    val itemGap = if (isDropDown) 0.dp else itemGap4
    val titleStyle = if (isDropDown) Style.title else Style.popupTitle

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = if (isCompact) 12.dp else 16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                modifier = Modifier.padding(
                    start = screenPadding,
                    end = screenPadding,
                    bottom = itemGap8
                ),
                text = title,
                style = titleStyle,
                color = theme.bodyText
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(itemGap)
            ) {
                items(5) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        ShimmerEffect(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> SelectorItem(
    isSingle: Boolean,
    isSelected: Boolean,
    optionData: OptionData<T>,
    onClick: (T) -> Unit,
    isDropDown: Boolean = false,
) {
    val icon = when {
        isSingle && isSelected -> Res.drawable.ic_radio_selected_line_24
        isSingle && !isSelected -> Res.drawable.ic_radio_unselect_line_24
        !isSingle && isSelected -> Res.drawable.ic_checkbox_selected_fill_24
        !isSingle && !isSelected -> Res.drawable.ic_checkbox_blank_line_24
        else -> null
    }

    val iconSize: Dp
    val textStyle: TextStyle

    if (isDropDown) {
        iconSize = 16.dp
        textStyle = Style.body
    } else {
        iconSize = 24.dp
        textStyle = Style.popupBody
    }

    CustomRipple(theme.primary) {
        Row(
            modifier = Modifier
                .clickable {
                    onClick(optionData.value)
                }
                .padding(
                    vertical = itemGap8,
                    horizontal = 16.dp
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = optionData.label,
                color = theme.bodyText,
                style = textStyle
            )
            icon?.let {
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = painterResource(it),
                    tint = theme.primary,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun SelectAll(
    isAllSelected: Boolean,
    isDropDown: Boolean = false,
    onClick: (Boolean) -> Unit,
) {
    val icon = if (isAllSelected) {
        Res.drawable.ic_checkbox_selected_fill_24
    } else {
        Res.drawable.ic_checkbox_blank_line_24
    }

    val iconSize: Dp
    val textStyle: TextStyle

    if (isDropDown) {
        iconSize = 16.dp
        textStyle = Style.body
    } else {
        iconSize = 24.dp
        textStyle = Style.popupBody
    }

    CustomRipple(theme.popUpBgSelected) {
        Row(
            modifier = Modifier
                .clickable {
                    onClick(!isAllSelected)
                }
                .padding(
                    vertical = itemGap8,
                    horizontal = 16.dp
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.label_select_all),
                color = theme.bodyText,
                style = textStyle
            )

            Icon(
                tint = theme.primary,
                modifier = Modifier.size(iconSize),
                painter = painterResource(icon),
                contentDescription = null
            )
        }

    }
}