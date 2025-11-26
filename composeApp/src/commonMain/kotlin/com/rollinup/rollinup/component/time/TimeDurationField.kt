package com.rollinup.rollinup.component.time


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.rollinup.common.utils.Utils.now
import com.rollinup.common.utils.Utils.toEpochMillis
import com.rollinup.common.utils.Utils.toLocalDateTime
import com.rollinup.rollinup.component.model.Platform
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextError
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.LocalGeneralSetting
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getPlatform
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime

@Composable
fun TimeDurationTextFieldTest() {
    var value by remember { mutableStateOf(listOf<Long?>()) }
    TimeDurationTextField(
        title = "Duration",
        value = value,
        onValueChange = { value = it }
    )
    Spacer(modifier = Modifier.height(24.dp))

}

@Composable
fun TimeDurationTextField(
    title: String,
    value: List<Long?>,
    onValueChange: (List<Long?>) -> Unit,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    isError: Boolean = false,
    textError: String? = null,
    enable: Boolean = true,
    titleStyle: TextStyle = Style.body,
) {
    TextFieldTitle(
        title = title,
        textStyle = titleStyle,
        isRequired = isRequired,
        color = theme.bodyText,
    ) {
        Column {
            TimeDurationTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier,
                isError = isError,
                enable = enable
            )
        }
        TextError(
            text = textError ?: "",
            isError = isError
        )
    }
}

@ExperimentalTime
@Composable
fun TimeDurationTextField(
    value: List<Long?>,
    onValueChange: (List<Long?>) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    enable: Boolean = true,
) {
    val generalSetting = LocalGeneralSetting.current
    val value = listOf(value.firstOrNull(), value.getOrNull(1))
    val from = value.firstOrNull()?.toLocalDateTime()
    val to = value.getOrNull(1)?.toLocalDateTime()
    val timeFrom = from?.time
    val timeTo = to?.time
    val isEmpty = value.isEmpty() || value.all { it == null }

    val min =
        if (timeFrom == null || timeFrom > generalSetting.schoolPeriodEnd) {
            generalSetting.schoolPeriodStart
        } else {
            timeFrom
        }
    val max =
        if (timeTo == null || timeTo < generalSetting.schoolPeriodStart) {
            generalSetting.schoolPeriodEnd
        } else {
            timeTo
        }


    var isFrom by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val lineColor: Color
    val textColor: Color

    when {
        isError -> {
            lineColor = theme.danger
            textColor = theme.danger
        }

        isEmpty -> {
            lineColor = theme.textPrimary
            textColor = theme.textPrimary.copy(alpha = 0.6f)
        }

        else -> {
            lineColor = theme.textPrimary
            textColor = theme.textPrimary
        }
    }

    val platform = getPlatform()


    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(itemGap8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .clickable(enable) {
                    isFrom = true
                    showBottomSheet = true
                }
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = timeFrom?.toString() ?: "Start",
                style = Style.title,
                color = textColor,
                modifier = Modifier.padding(vertical = itemGap8)
            )
            HorizontalDivider(color = lineColor)
        }

        Text(
            text = " - ",
            style = Style.title,
            color = textColor
        )

        Column(
            modifier = Modifier
                .clickable(enable) {
                    isFrom = false
                    showBottomSheet = true
                }
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = timeTo?.toString() ?: "End",
                style = Style.title,
                color = textColor,
                modifier = Modifier.padding(vertical = itemGap8)
            )
            HorizontalDivider(color = lineColor)
        }
    }

    when (platform) {
        Platform.ANDROID, Platform.IOS -> {
            TimePickerBottomSheet(
                min = min,
                max = max,
                showSheet = showBottomSheet,
                onDismissRequest = { showBottomSheet = it },
                onValueChange = { time ->
                    val newValue = if (isFrom) {
                        buildList {
                            add(LocalDateTime(LocalDate.now(), time).toEpochMillis())
                            add(value.getOrNull(1))
                        }
                    } else {
                        buildList {
                            add(value.getOrNull(0))
                            add(LocalDateTime(LocalDate.now(), time).toEpochMillis())
                        }
                    }
                    onValueChange(newValue)
                },
            )
        }

        else -> {
            TimePickerBottomSheet(
                min = min,
                max = max,
                showSheet = showBottomSheet,
                onDismissRequest = { showBottomSheet = it },
                onValueChange = { time ->
                    val newValue = if (isFrom) {
                        buildList {
                            add(LocalDateTime(LocalDate.now(), time).toEpochMillis())
                            add(value.getOrNull(1))
                        }
                    } else {
                        buildList {
                            add(value.getOrNull(0))
                            add(LocalDateTime(LocalDate.now(), time).toEpochMillis())
                        }
                    }
                    onValueChange(newValue)
                },
            )
        }
    }
}