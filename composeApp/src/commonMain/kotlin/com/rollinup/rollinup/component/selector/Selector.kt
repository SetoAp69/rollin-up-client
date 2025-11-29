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
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.model.OptionData
import com.rollinup.rollinup.component.model.Platform
import com.rollinup.rollinup.component.ripple.CustomRipple
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.textfield.TextError
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
    value: List<T>,
    enable: Boolean = true,
    contentColor: Color = theme.textPrimary,
    backgroundColor: Color = theme.secondary,
    placeHolder: String = "-",
    options: List<OptionData<T>>,
    width: Dp? = 100.dp,
    onValueChange: (List<T>) -> Unit,
) {
    var showSelector by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (showSelector) 90f else 0F)

    val sValue = when {
        value.size == 1 -> options.find { it.value == value }?.label?.take(10) ?: ""
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
    width: Dp? = 100.dp,
    options: List<OptionData<T>>,
    onValueChange: (T) -> Unit,
) {
    TextFieldTitle(
        title = title,
    ) {
        var showSelector by remember { mutableStateOf(false) }
        val rotationState by animateFloatAsState(targetValue = if (showSelector) 90f else 0F)

        val sValue = value?.toString()?.let {
            if (it.length > 10) "${it.take(7)}..." else it
        } ?: placeHolder

        val modifier = width?.let {
            Modifier.width(it)
        } ?: Modifier.fillMaxWidth()

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
            onValueChange = onValueChange,
            onDismissRequest = { showSelector = it },
            isShowSelector = showSelector,
        )
    }
}

@Composable
fun SelectorTest() {
    var multiSelectorValue by remember { mutableStateOf(listOf<Int>()) }
    var singleSelectorValue: Int? by remember { mutableStateOf(null) }
    var showSingleSelector: Boolean by remember { mutableStateOf(false) }
    var showMultiSelector: Boolean by remember { mutableStateOf(false) }

    val options = (1..20).map {
        OptionData(
            label = "Option $it",
            value = it
        )
    }

    Column {
        Text(
            text = "$singleSelectorValue",
            style = Style.body,
            color = theme.bodyText
        )
        Spacer(itemGap4)
        Text(
            text = "$multiSelectorValue",
            style = Style.body,
            color = theme.bodyText
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(modifier = Modifier.weight(1f), text = "Single") {
                showSingleSelector = true
            }
            Spacer(itemGap8)
            Button(modifier = Modifier.weight(1f), text = "Multi") {
                showMultiSelector = true
            }
        }

        SingleSelector(
            isShowSelector = showSingleSelector,
            onDismissRequest = { showSingleSelector = it },
            title = "Balls",
            value = singleSelectorValue,
            options = options,
            onValueChange = {
                singleSelectorValue = it
            }
        )

        MultiSelector(
            isShowSelector = showMultiSelector,
            onDismissRequest = { showMultiSelector = it },
            title = "MultiBallverse",
            value = multiSelectorValue,
            options = options,
            onValueChange = {
                multiSelectorValue = it
            }
        )
    }


}

@Composable
fun <T> SingleSelector(
    isShowSelector: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    title: String,
    value: T?,
    options: List<OptionData<T>>,
    onValueChange: (T) -> Unit,
) {
    when (getPlatform()) {
        Platform.IOS, Platform.ANDROID -> {
            SingleSelectorBottomSheet(
                isShowSelector = isShowSelector,
                onDismissRequest = onDismissRequest,
                title = title,
                value = value,
                options = options,
                onValueChange = onValueChange
            )
        }

        else -> {
            SingleSelectorDialog(
                isShowSelector = isShowSelector,
                onDismissRequest = onDismissRequest,
                title = title,
                value = value,
                options = options,
                onValueChange = onValueChange
            )
        }
//        else -> {
//            SingleSelectorDropDown(
//                isShowSelector = isShowSelector,
//                onDismissRequest = onDismissRequest,
//                title = title,
//                value = value,
//                options = options,
//                onValueChange = onValueChange
//            )
//        }
    }
}

@Composable
fun <T> SingleSelectorDropDown(
    isShowSelector: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    title: String,
    value: T?,
    options: List<OptionData<T>>,
    onValueChange: (T) -> Unit,
) {
    val height = getScreenHeight() * 0.56f
    val width = getScreenHeight() * 0.15f
    DropDownMenu(
        onDismissRequest = { onDismissRequest(false) },
        isShowDropDown = isShowSelector
    ) {
        Box(
            modifier = Modifier
                .heightIn(max = height)
                .widthIn(max = width)
                .background(
                    color = theme.popUpBg,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.TopEnd
        ) {
            Icon(
                modifier = Modifier
                    .padding(if (isCompact) 12.dp else 16.dp)
                    .clip(CircleShape)
                    .clickable {
                        onDismissRequest(false)
                    }
                    .size(32.dp),
                tint = theme.lineStroke,
                painter = painterResource(Res.drawable.ic_close_line_24),
                contentDescription = null
            )
            SingleSelectorContent(
                title = title,
                value = value,
                isDropDown = true,
                options = options,
                onValueChange = onValueChange,
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@Composable
fun <T> SingleSelectorDialog(
    isShowSelector: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    title: String,
    value: T?,
    options: List<OptionData<T>>,
    onValueChange: (T) -> Unit,
) {
    val height = getScreenHeight()
    val width = getScreenWidth()
    val properties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = false
    )
    if (isShowSelector) {
        Dialog(
            onDismissRequest = { onDismissRequest(false) },
            properties = properties
        ) {
            Box(
                modifier = Modifier
                    .size(width * 0.2f, height * 0.56f)
                    .background(
                        color = theme.popUpBg,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.TopEnd
            ) {
                Icon(
                    modifier = Modifier
                        .padding(if (isCompact) 12.dp else 16.dp)
                        .clip(CircleShape)
                        .clickable {
                            onDismissRequest(false)
                        }
                        .size(32.dp),
                    tint = theme.lineStroke,
                    painter = painterResource(Res.drawable.ic_close_line_24),
                    contentDescription = null
                )
                SingleSelectorContent(
                    title = title,
                    value = value,
                    options = options,
                    onValueChange = onValueChange,
                    onDismissRequest = onDismissRequest
                )
            }
        }
    }

}


@Composable
fun <T> SingleSelectorBottomSheet(
    isShowSelector: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    title: String,
    value: T?,
    options: List<OptionData<T>>,
    onValueChange: (T) -> Unit,
) {
    val height = getScreenHeight()
    BottomSheet(
        modifier = Modifier.heightIn(max = height * 0.8f),
        isShowSheet = isShowSelector,
        onDismissRequest = onDismissRequest
    ) {
        SingleSelectorContent(
            title = title,
            value = value,
            options = options,
            onValueChange = onValueChange,
            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
private fun <T> SingleSelectorContent(
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
fun <T> MultiSelector(
    isShowSelector: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    title: String,
    value: List<T>,
    options: List<OptionData<T>>,
    onValueChange: (List<T>) -> Unit,
) {
    when (getPlatform()) {
        Platform.IOS, Platform.ANDROID -> {
            MultiSelectorBottomSheet(
                isShowSelector = isShowSelector,
                onDismissRequest = onDismissRequest,
                title = title,
                value = value,
                options = options,
                onValueChange = onValueChange
            )
        }

//        else -> {
//            MultiSelectorDialog(
//                isShowSelector = isShowSelector,
//                onDismissRequest = onDismissRequest,
//                title = title,
//                value = value,
//                options = options,
//                onValueChange = onValueChange
//            )
//        }
        else -> {
            MultiSelectorDropDown(
                isShowSelector = isShowSelector,
                onDismissRequest = onDismissRequest,
                title = title,
                value = value,
                options = options,
                onValueChange = onValueChange
            )
        }

    }
}

@Composable
fun <T> MultiSelectorDropDown(
    isShowSelector: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    title: String,
    value: List<T>,
    options: List<OptionData<T>>,
    onValueChange: (List<T>) -> Unit,
) {
    val height = getScreenHeight() * 0.3f
    val width = getScreenHeight() * 0.2f

    DropDownMenu(
        onDismissRequest = { onDismissRequest(false) },
        isShowDropDown = isShowSelector
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .background(
                    color = theme.popUpBg,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.TopEnd
        ) {
            MultiSelectorContent(
                title = title,
                value = value,
                options = options,
                isDropDown = true,
                onValueChange = {
                    onValueChange(it)
                    onDismissRequest(false)
                }
            )
        }

    }

}

@Composable
fun <T> MultiSelectorDialog(
    isShowSelector: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    title: String,
    value: List<T>,
    options: List<OptionData<T>>,
    onValueChange: (List<T>) -> Unit,
) {
    val height = getScreenHeight()
    val width = getScreenWidth()
    val properties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = false
    )
    if (isShowSelector) {
        Dialog(
            onDismissRequest = { onDismissRequest(false) },
            properties = properties
        ) {
            Box(
                modifier = Modifier
                    .width(width * 0.2f)
                    .heightIn(max = height * 0.56f)
                    .background(
                        color = theme.popUpBg,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.TopEnd
            ) {
                MultiSelectorContent(
                    title = title,
                    value = value,
                    options = options,
                    onValueChange = {
                        onValueChange(it)
                        onDismissRequest(false)
                    }
                )
                Icon(
                    modifier = Modifier
                        .padding(if (isCompact) 12.dp else 16.dp)
                        .clip(CircleShape)
                        .clickable {
                            onDismissRequest(false)
                        }
                        .size(32.dp),
                    tint = theme.lineStroke,
                    painter = painterResource(Res.drawable.ic_close_line_24),
                    contentDescription = null
                )
            }
        }
    }

}


@Composable
fun <T> MultiSelectorBottomSheet(
    isShowSelector: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    title: String,
    value: List<T>,
    options: List<OptionData<T>>,
    onValueChange: (List<T>) -> Unit,
) {
    val height = getScreenHeight()
    BottomSheet(
        modifier = Modifier.heightIn(max = height * 0.8f),
        isShowSheet = isShowSelector,
        onDismissRequest = onDismissRequest
    ) {
        MultiSelectorContent(
            title = title,
            value = value,
            options = options,
            onValueChange = {
                onValueChange(it)
                onDismissRequest(false)
            }
        )
    }
}

@Composable
private fun <T> MultiSelectorContent(
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