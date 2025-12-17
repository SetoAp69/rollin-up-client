package com.rollinup.rollinup.component.selector

import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
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
    SelectorBottomSheet(
        isShowSelector = isShowSelector,
        onDismissRequest = onDismissRequest,
    ) {
        if (isLoading) {
            SelectorLoading(
                title = title,
                isDropDown = false
            )
        } else {
            MultiSelectorContent(
                title = title,
                value = value,
                isDropDown = false,
                options = options,
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
    SelectorBottomSheet(
        isShowSelector = isShowSelector,
        onDismissRequest = onDismissRequest
    ) {
        if (isLoading) {
            SelectorLoading(
                title = title,
                isDropDown = false
            )
        } else {
            SingleSelectorContent(
                title = title,
                isDropDown = false,
                value = value,
                options = options,
                onValueChange = onValueChange,
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@Composable
private fun SelectorBottomSheet(
    isShowSelector: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    val height = getScreenHeight()
    BottomSheet(
        modifier = Modifier.heightIn(max = height * 0.8f),
        isShowSheet = isShowSelector,
        onDismissRequest = onDismissRequest
    ) {
        content()
    }
}
