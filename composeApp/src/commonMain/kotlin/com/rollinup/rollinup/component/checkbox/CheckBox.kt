package com.rollinup.rollinup.component.checkbox

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rollinup.rollinup.component.theme.theme

/**
 * A styled wrapper for the Material3 Checkbox.
 *
 * This component applies the application's default theme colors to the standard checkbox.
 *
 * @param checked Whether the checkbox is checked.
 * @param onCheckedChange Callback invoked when the checked state changes.
 * @param modifier Modifier to be applied to the layout.
 * @param enabled Whether the component is enabled.
 * @param colors The colors for the checkbox states. Defaults to [CheckBoxDefaults.colors].
 */
@Composable
fun CheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckBoxDefaults.colors,
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
    )
}

/**
 * Default values and theme mappings for the CheckBox component.
 */
object CheckBoxDefaults {
    val colors
        @Composable
        get() = CheckboxColors(
            checkedCheckmarkColor = theme.popUpBg,
            uncheckedCheckmarkColor = Color.Transparent,
            checkedBoxColor = theme.primary,
            uncheckedBoxColor = Color.Transparent,
            disabledCheckedBoxColor = theme.textFieldBgDisabled,
            disabledUncheckedBoxColor = theme.textFieldBgDisabled,
            disabledIndeterminateBoxColor = theme.textFieldBgDisabled,
            checkedBorderColor = theme.primary,
            uncheckedBorderColor = theme.primary,
            disabledBorderColor = theme.textFieldStrokeDisabled,
            disabledUncheckedBorderColor = theme.textFieldStrokeDisabled,
            disabledIndeterminateBorderColor = Color.Transparent
        )
}