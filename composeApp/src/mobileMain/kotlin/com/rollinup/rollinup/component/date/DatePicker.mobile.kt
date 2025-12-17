package com.rollinup.rollinup.component.date

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.button.ButtonType
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getScreenHeight
import com.rollinup.rollinup.component.utils.isCompact
import kotlinx.datetime.LocalDate

@Composable
actual fun SingleDatePicker(
    isShowDatePicker: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    value: LocalDate?,
    onSelectDate: (LocalDate?) -> Unit,
    isDisablePastSelection: Boolean,
    isAllSelectable: Boolean,
    color: DatePickerColor,
) {
    var currentValue by remember { mutableStateOf(value) }

    LaunchedEffect(isShowDatePicker) {
        if (value != currentValue && isShowDatePicker) currentValue = value
    }

    DatePickerBottomSheet(
        isShowSheet = isShowDatePicker,
        onDismissRequest = onDismissRequest,
        onClickReset = { currentValue = null },
        onClickConfirm = { onSelectDate(currentValue); onDismissRequest(false) },
    ) {
        DatePickerCalendar(
            value = currentValue,
            onSelectDate = { value ->
                currentValue = value
            },
            isDisablePastSelection = isDisablePastSelection,
            color = color,
            isAllSelectable = isAllSelectable
        )
    }
}

@Composable
actual fun DateRangePicker(
    isShowDatePicker: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    value: List<LocalDate>,
    onSelectDate: (List<LocalDate>) -> Unit,
    title: String,
    maxRange: Int,
    isDisablePastSelection: Boolean,
    isAllSelectable: Boolean,
    color: DatePickerColor,
) {
    var currentValue by remember { mutableStateOf(value) }

    LaunchedEffect(isShowDatePicker) {
        if (value != currentValue && isShowDatePicker) currentValue = value
    }

    DatePickerBottomSheet(
        isShowSheet = isShowDatePicker,
        onDismissRequest = onDismissRequest,
        title = title,
        onClickReset = { currentValue = emptyList() },
        onClickConfirm = { onSelectDate(currentValue); onDismissRequest(false) },
    ) {
        DateRangePickerCalendar(
            value = currentValue,
            onSelectDate = { value ->
                currentValue = value
            },
            isDisablePastSelection = isDisablePastSelection,
            color = color,
            isAllSelectable = isAllSelectable
        )
    }
}

@Composable
private fun DatePickerBottomSheet(
    isShowSheet: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onClickConfirm: () -> Unit,
    onClickReset: () -> Unit,
    title:String = "",
    content: @Composable () -> Unit,
) {
    BottomSheet(
        isShowSheet = isShowSheet,
        modifier = Modifier
            .height(getScreenHeight() * 0.52f),
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isCompact) 12.dp else 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(16.dp)
            if(title.isNotBlank()){
                Text(
                    text = title,
                    color = theme.bodyText,
                    style = Style.popupTitle
                )
            }
            content()
            Spacer(modifier = Modifier.weight(1f))
            Row {
                Button(
                    modifier = Modifier.weight(1f),
                    text = "Reset",
                    type = ButtonType.OUTLINED,
                ) {
                    onClickReset()
                }
                Spacer(itemGap8)
                Button(
                    modifier = Modifier.weight(1f),
                    text = "Apply",
                ) {
                    onClickConfirm()
                }

            }
        }
    }
}