package com.rollinup.rollinup.screen.main.screen.globalsetting.ui.screen.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rollinup.rollinup.component.date.DateFormatter
import com.rollinup.rollinup.component.textfield.BaseTextField
import com.rollinup.rollinup.component.textfield.TextError
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.time.TimePickerDropDown
import kotlinx.datetime.LocalTime

@Composable
fun TimePickerTextField(
    title: String,
    placeholder: String,
    value: Long?,
    onValueChange: (Long) -> Unit,
    isError: Boolean = false,
    errorText: String? = null,
    onError: (String) -> Unit,
) {
    TextFieldTitle(
        title = title
    ) {
        TimePickerTextField(
            placeholder = placeholder,
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            errorText = errorText,
            onError = onError
        )
        TextError(
            text = errorText ?: "",
            isError = isError
        )
    }
}

@Composable
private fun TimePickerTextField(
    placeholder: String,
    value: Long?,
    onValueChange: (Long) -> Unit,
    isError: Boolean = false,
    errorText: String? = null,
    onError: (String) -> Unit,
) {
    val time = value?.let {
        LocalTime.fromSecondOfDay(it.toInt())
    }

    var showDropDown by remember { mutableStateOf(false) }
    var stringValue by remember { mutableStateOf("") }

    var errorMessage: String? = errorText

    LaunchedEffect(value) {
        stringValue = time?.let {
            errorMessage = null
            DateFormatter.formatTime(it)
        } ?: placeholder
    }
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier.clickable {
                showDropDown = true
            }
        ) {
            BaseTextField(
                value = stringValue,
                onValueChange = { value ->
                    if (value.isBlank()) return@BaseTextField
                    try {
                        val timeValue = LocalTime.parse(stringValue)
                        onValueChange(timeValue.toSecondOfDay().toLong())

                    } catch (e: IllegalArgumentException) {
                        stringValue = value
                        errorMessage = "Invalid time format"
                        onError(errorMessage!!)
                    }
                },
                isEnabled = false,
                placeholder = placeholder,
                modifier = Modifier.clickable {
                    showDropDown = true
                },
                isReadOnly = true,
                isError = isError
            )
        }
        TimePickerDropDown(
            showDropdown = showDropDown,
            onValueChange = { time ->
                onValueChange(
                    time
                        .toSecondOfDay()
                        .toLong()
                )
            },
            onDismissRequest = { showDropDown = it },
            min = LocalTime(0, 0, 0),
            max = LocalTime(23, 59, 59),
        )
    }


}

