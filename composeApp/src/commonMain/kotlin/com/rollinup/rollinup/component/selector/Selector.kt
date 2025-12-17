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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.model.Platform
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
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.component.utils.getScreenHeight
import com.rollinup.rollinup.component.utils.getScreenWidth
import com.rollinup.rollinup.component.utils.isCompact
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_checkbox_blank_line_24
import rollin_up.composeapp.generated.resources.ic_checkbox_selected_fill_24
import rollin_up.composeapp.generated.resources.ic_close_line_24
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_right_24
import rollin_up.composeapp.generated.resources.ic_radio_selected_line_24
import rollin_up.composeapp.generated.resources.ic_radio_unselect_line_24

@Composable
fun <T> SingleSelectorField(
    title: String,
    value: T?,
    options: List<OptionData<T>>,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    textError: String? = null,
    onValueChange: (T) -> Unit,
    placeHolder: String = "Select",
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
                .padding(itemGap4),
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

@Composable
fun <T> SingleDropDownSelector(
    title: String,
    value: T?,
    enable: Boolean = true,
    contentColor: Color = theme.textPrimary,
    backgroundColor: Color = theme.secondary,
    placeHolder: String = "-",
    isError:Boolean = false,
    width: Dp? = 100.dp,
    isLoading:Boolean = false,
    options: List<OptionData<T>>,
    onValueChange: (T) -> Unit,
) {
    val backgroundColor = if(isError) theme.textFieldBgError else backgroundColor
    val contentColor = if(isError) theme.danger else contentColor

    var showSelector by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (showSelector) 90f else 0F)

    val sValue = options.find {
        it.value == value
    }?.label?:placeHolder

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
            text = "Apply"
        ) {
            onValueChange(tempValue)
        }
    }
}

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
                text = "Select All",
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