package com.rollinup.rollinup.component.selector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getScreenHeight

@Composable
actual fun <T> MultiSelector(
    isShowSelector: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    title: String,
    value: List<T>,
    options: List<OptionData<T>>,
    isLoading: Boolean,
    onValueChange: (List<T>) -> Unit,
) {
    SelectorDropDown(
        isShowSelector = isShowSelector,
        onDismissRequest = onDismissRequest,
    ) {
        if (isLoading) {
            SelectorLoading(
                title = title,
                isDropDown = true
            )
        } else {
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
actual fun <T> SingleSelector(
    isShowSelector: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    title: String,
    value: T?,
    options: List<OptionData<T>>,
    isLoading: Boolean,
    onValueChange: (T) -> Unit,
) {
    SelectorDropDown(
        isShowSelector = isShowSelector,
        onDismissRequest = onDismissRequest,
    ) {
        if (isLoading) {
            SelectorLoading(title = title, isDropDown = true)
        } else {
            SingleSelectorContent(
                title = title,
                isDropDown = true,
                value = value,
                options = options,
                onValueChange = onValueChange,
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@Composable
private fun SelectorDropDown(
    isShowSelector: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    val height = 400.dp
    val width = 200.dp

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
            content()
        }
    }
}
