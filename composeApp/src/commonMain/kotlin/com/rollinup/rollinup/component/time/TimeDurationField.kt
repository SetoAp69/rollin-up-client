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

/**
 * A compound form field for selecting a time duration (Start Time and End Time).
 *
 * This component renders two interactive time selection fields side-by-side.
 * It automatically adapts its interaction model based on the platform:
 * - **Mobile (Android/iOS):** Opens a [TimePickerBottomSheet].
 * - **Desktop/Web:** Opens a [TimePickerDropDown].
 *
 * @param value A list containing exactly two [LocalTime] elements (Start, End). Elements can be null.
 * @param onValueChange Callback invoked when either time value changes. Returns the updated list.
 * @param title The main label displayed above the field group.
 * @param textError Error message to display below the fields if [isError] is true.
 * @param isRequired If true, displays a visual indicator (asterisk) next to the title.
 * @param isError If true, applies error styling to the text and borders.
 * @param modifier Modifier applied to the container row.
 * @param placeHolder Text to display when a time value is null (e.g., "-").
 */
@Composable
fun TimeDurationTextField(
    value: List<LocalTime?>,
    onValueChange: (List<LocalTime?>) -> Unit,
    title: String,
    textError: String? = null,
    isRequired: Boolean = false,
    isError: Boolean = false,
    modifier: Modifier = Modifier,
    placeHolder: String = "-",
) {
    TextFieldTitle(
        title = title,
        isRequired = isRequired
    ) {
        Column {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(itemGap8)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    TimeDurationItem(
                        value = value,
                        onValueChange = onValueChange,
                        isFrom = true,
                        isError = isError,
                        placeHolder = placeHolder
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.weight(0.2f),
                    color = theme.textPrimary,
                    thickness = itemGap8
                )
                Box(modifier = Modifier.weight(1f)) {
                    TimeDurationItem(
                        value = value,
                        onValueChange = onValueChange,
                        isFrom = false,
                        isError = isError,
                        placeHolder = placeHolder
                    )
                }
            }
            TextError(
                text = textError ?: "",
                isError = isError
            )
        }
    }
}

/**
 * An individual interactive item representing either the Start or End time.
 *
 * Handles the click interaction to trigger the appropriate platform-specific picker
 * and updates the specific index in the value list.
 *
 * @param value The current list of [LocalTime] values.
 * @param onValueChange Callback to update the list.
 * @param isFrom True if this item represents the Start time (index 0), False for End time (index 1).
 * @param isError Controls the text color for error states.
 * @param placeHolder Text displayed when the specific time value is null.
 */
@Composable
private fun TimeDurationItem(
    value: List<LocalTime?>,
    onValueChange: (List<LocalTime?>) -> Unit,
    isFrom: Boolean,
    isError: Boolean,
    placeHolder: String,
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    val selectedValue = if (isFrom) value.getOrNull(0) else value.getOrNull(1)
    val textStyle: TextStyle
    val textColor: Color

    val label =
        if (isFrom) stringResource(Res.string.label_end) else stringResource(Res.string.label_start)

    if (selectedValue != null) {
        textStyle = Style.title
        textColor = if (isError) theme.danger else theme.textPrimary
    } else {
        textStyle = Style.body
        textColor = if (isError) theme.danger else theme.textPrimary.copy(alpha = 0.5f)
    }

    Box {
        Column(
            modifier = Modifier.clickable {
                showBottomSheet = true
            }
        ) {
            Text(
                text = label,
                color = theme.textPrimary,
                style = Style.label,
                modifier = Modifier.padding(bottom = itemGap8)
            )
            Text(
                text = selectedValue?.toString() ?: placeHolder,
                color = textColor,
                style = textStyle,
                modifier = Modifier
                    .padding(bottom = itemGap8)
                    .fillMaxWidth()
            )
            HorizontalDivider(
                color = textColor,
                thickness = itemGap8
            )
        }

        when (getPlatform()) {
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