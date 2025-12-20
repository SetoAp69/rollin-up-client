package com.rollinup.rollinup.component.time


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.rollinup.rollinup.component.model.Platform
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextError
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getPlatform
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_end
import rollin_up.composeapp.generated.resources.label_start
import kotlin.time.ExperimentalTime

@Composable
fun TimeDurationTextField(
    title: String,
    value: List<LocalTime?>,
    onValueChange: (List<LocalTime?>) -> Unit,
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
    value: List<LocalTime?>,
    onValueChange: (List<LocalTime?>) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    enable: Boolean = true,
) {
    val value = listOf(value.firstOrNull(), value.getOrNull(1))
    val from = value.firstOrNull()
    val to = value[1]

    val isEmpty = value.isEmpty() || value.all { it == null }

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

    Box(contentAlignment = Alignment.Center) {
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
                    text = from?.toString() ?: stringResource(Res.string.label_start),
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
                    text = to?.toString() ?: stringResource(Res.string.label_end),
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
                    showSheet = showBottomSheet,
                    onDismissRequest = { showBottomSheet = it },
                    onValueChange = { time ->
                        val newValue = if (isFrom) {
                            buildList {
                                add(time)
                                add(value.getOrNull(1))
                            }
                        } else {
                            buildList {
                                add(value.getOrNull(0))
                                add(time)
                            }
                        }
                        onValueChange(newValue)
                    },
                )
            }

            else -> {
                TimePickerDropDown(
                    showDropdown = showBottomSheet,
                    onDismissRequest = { showBottomSheet = it },
                    onValueChange = { time ->
                        val newValue = if (isFrom) {
                            buildList {
                                add(time)
                                add(value.getOrNull(1))
                            }
                        } else {
                            buildList {
                                add(value.getOrNull(0))
                                add(time)
                            }
                        }
                        onValueChange(newValue)
                    },
                )
            }
        }
    }
}