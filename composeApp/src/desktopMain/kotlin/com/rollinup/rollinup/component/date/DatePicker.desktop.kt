package com.rollinup.rollinup.component.date

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.button.ButtonType
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getScreenHeight
import com.rollinup.rollinup.component.utils.getScreenWidth
import com.rollinup.rollinup.component.utils.isCompact
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_close_line_24

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

    DatePickerDialog(
        isShowDialog = isShowDatePicker,
        onDismissRequest = onDismissRequest,
        onClickApply = {
            onSelectDate(currentValue)
        },
        onClickReset = {
            currentValue = null
        }
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

    DatePickerDialog(
        isShowDialog = isShowDatePicker,
        onDismissRequest = onDismissRequest,
        title = title,
        onClickApply = {
            onSelectDate(currentValue)
        },
        onClickReset = {
            currentValue = emptyList()
        }
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
private fun DatePickerDialog(
    isShowDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onClickApply: () -> Unit,
    onClickReset: () -> Unit,
    title: String = "",
    content: @Composable () -> Unit,
) {
    val width = getScreenWidth() * 0.2f
    val height = getScreenHeight() * 0.56f

    if (isShowDialog) {
        Dialog(
            onDismissRequest = {
                onDismissRequest(false)
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Column(
                modifier = Modifier
                    .background(
                        shape = RoundedCornerShape(12.dp),
                        color = theme.popUpBg
                    )
                    .sizeIn(
                        minWidth = 360.dp,
                        minHeight = 526.dp,
                        maxWidth = if (width > 360.dp) width else 360.dp,
                        maxHeight = if (height > 526.dp) height else 526.dp
                    )
                    .padding(if (isCompact) 18.dp else 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (title.isNotBlank()) {
                    Text(
                        text = title,
                        color = theme.bodyText,
                        style = Style.popupTitle
                    )
                    Spacer(itemGap8)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Icon(
                        modifier = Modifier
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
                content()
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
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
                        onClickApply()
                        onDismissRequest(false)
                    }
                }
            }
        }
    }
}