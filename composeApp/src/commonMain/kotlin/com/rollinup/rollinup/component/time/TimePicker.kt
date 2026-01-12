@file:OptIn(ExperimentalTime::class)

package com.rollinup.rollinup.component.time

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rollinup.common.utils.Utils.now
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.common.utils.Utils.toEpochMillis
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.textfield.TextError
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.LocalGlobalSetting
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.globalSetting
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getPlatform
import dev.darkokoa.datetimewheelpicker.WheelTimePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import dev.darkokoa.datetimewheelpicker.core.format.TimeFormat
import dev.darkokoa.datetimewheelpicker.core.format.timeFormatter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_apply
import kotlin.time.ExperimentalTime

@Composable
fun TimePickerTextField(
    title: String,
    placeholder: String,
    value: Long?,
    onValueChange: (Long) -> Unit,
    isRequired: Boolean = false,
    isError: Boolean = false,
    errorText: String? = null,
    enabled: Boolean = true,
    max: LocalTime? = null,
    min: LocalTime? = null,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val dateValue = value?.parseToLocalDateTime(TimeZone.UTC)
    val stringValue = dateValue?.time?.toString() ?: placeholder

    val textStyle = if (value == null) Style.body else Style.title
    val lineColor: Color
    val textColor: Color

    val maxTime = max ?: globalSetting.schoolPeriodEnd
    val minTime = min ?: globalSetting.checkInPeriodStart

    val generalSetting = LocalGlobalSetting.current

    when {
        isError -> {
            lineColor = theme.danger
            textColor = theme.danger
        }

        !enabled -> {
            lineColor = theme.textFieldStrokeDisabled
            textColor = theme.textFieldStrokeDisabled
        }

        value == null -> {
            textColor = theme.textPrimary.copy(alpha = 0.4f)
            lineColor = theme.textPrimary
        }

        else -> {
            textColor = theme.textPrimary
            lineColor = theme.textPrimary
        }
    }

    TextFieldTitle(
        title = title,
        isRequired = isRequired
    ) {
        Column {
            Column(
                modifier = Modifier
                    .clickable(
                        enabled = enabled
                    ) {
                        showBottomSheet = true
                    }
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = itemGap8)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringValue,
                    style = textStyle,
                    color = textColor
                )
                HorizontalDivider(color = lineColor, thickness = 1.dp)
            }
            Spacer(itemGap4)
            TextError(
                text = errorText ?: "",
                isError = isError
            )
        }
    }

    if (getPlatform().isMobile()) {
        TimePickerBottomSheet(
            showSheet = showBottomSheet,
            onValueChange = { time ->
                val date = dateValue?.date ?: LocalDate.now()

                onValueChange(
                    LocalDateTime(date, time).toEpochMillis()
                )
            },
            onDismissRequest = { showBottomSheet = it },
            min = generalSetting.checkInPeriodStart,
            max = generalSetting.schoolPeriodEnd,
        )
    } else {
        TimePickerDialog(
            showDialog = showBottomSheet,
            onValueChange = { time ->
                val date = dateValue?.date ?: LocalDate.now()

                onValueChange(
                    LocalDateTime(date, time).toEpochMillis()
                )
            },
            onDismissRequest = { showBottomSheet = it },
            min = generalSetting.checkInPeriodStart,
            max = generalSetting.schoolPeriodEnd,
        )
    }
}

@Composable
fun TimePickerDropDown(
    showDropdown: Boolean,
    onValueChange: (LocalTime) -> Unit,
    onDismissRequest: (Boolean) -> Unit,
    min: LocalTime = LocalTime(0, 0, 0),
    max: LocalTime = LocalTime(23, 59, 59),
    timeFormat: TimeFormat = TimeFormat.HOUR_24,
) {
    var tempValue by remember { mutableStateOf(LocalTime.fromSecondOfDay(0)) }

    DropDownMenu(
        isShowDropDown = showDropdown,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = theme.popUpBg,
                    shape = RoundedCornerShape(screenPadding)
                )
                .width(200.dp)
                .height(400.dp)
                .padding(vertical = screenPadding)
        ) {
            TimePicker(
                modifier = Modifier.weight(1f),
                onValueChange = {
                    tempValue = it
                },
                min = min,
                max = max,
                timeFormat = timeFormat,
            )
            Spacer(itemGap8)
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenPadding),
                text = stringResource(Res.string.label_apply)
            ) {
                onValueChange(tempValue)
                onDismissRequest(false)
            }
        }
    }
}

@Composable
fun TimePickerDialog(
    showDialog: Boolean,
    onValueChange: (LocalTime) -> Unit,
    onDismissRequest: (Boolean) -> Unit,
    min: LocalTime = LocalTime(0, 0, 0),
    max: LocalTime = LocalTime(23, 59, 59),
    timeFormat: TimeFormat = TimeFormat.HOUR_24,
) {
    var tempValue by remember { mutableStateOf(LocalTime.fromSecondOfDay(0)) }

    val properties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = false
    )

    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismissRequest(false) },
            properties = properties,
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = theme.popUpBg,
                        shape = RoundedCornerShape(screenPadding)
                    )
                    .width(200.dp)
                    .height(400.dp)
                    .padding(vertical = screenPadding)
            ) {
                TimePicker(
                    modifier = Modifier.weight(1f),
                    onValueChange = {
                        tempValue = it
                    },
                    min = min,
                    max = max,
                    timeFormat = timeFormat,
                )
                Spacer(itemGap8)
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenPadding),
                    text = stringResource(Res.string.label_apply)
                ) {
                    onValueChange(tempValue)
                    onDismissRequest(false)
                }
            }
        }
    }
}

@Composable
fun TimePickerBottomSheet(
    showSheet: Boolean,
    onValueChange: (LocalTime) -> Unit,
    onDismissRequest: (Boolean) -> Unit,
    min: LocalTime = LocalTime(0, 0, 0),
    max: LocalTime = LocalTime(23, 59, 59),
    timeFormat: TimeFormat = TimeFormat.HOUR_24,
) {
    var tempValue by remember { mutableStateOf(LocalTime.fromSecondOfDay(0)) }

    BottomSheet(
        modifier = Modifier.fillMaxHeight(0.4f),
        onDismissRequest = onDismissRequest,
        isShowSheet = showSheet
    ) {
        TimePicker(
            modifier = Modifier.weight(1f),
            onValueChange = {
                tempValue = it
            },
            min = min,
            max = max,
            timeFormat = timeFormat,
        )
        Spacer(itemGap8)
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = stringResource(Res.string.label_apply)
        ) {
            onValueChange(tempValue)
            onDismissRequest(false)
        }
    }
}

@Composable
fun TimePicker(
    onValueChange: (LocalTime) -> Unit,
    min: LocalTime? = null,
    max: LocalTime? = null,
    timeFormat: TimeFormat = TimeFormat.HOUR_24,
    modifier: Modifier = Modifier,
) {
    val properties = WheelPickerDefaults.selectorProperties(
        color = theme.popUpBgSelected,
        border = null,
        shape = RectangleShape
    )

    val timeFormatter = timeFormatter(timeFormat)

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
    ) {
        WheelTimePicker(
            modifier = Modifier.fillMaxWidth(),
            maxTime = max ?: LocalTime(23, 59, 59),
            minTime = min ?: LocalTime(0, 0, 0),
            timeFormatter = timeFormatter,
            size = DpSize(maxWidth, maxHeight),
            rowCount = 5,
            textStyle = Style.popupTitle,
            textColor = theme.primary,
            selectorProperties = properties,
            onSnappedTime = onValueChange
        )
    }
}
