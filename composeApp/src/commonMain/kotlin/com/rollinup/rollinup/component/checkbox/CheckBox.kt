package com.rollinup.rollinup.component.checkbox

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rollinup.rollinup.component.theme.theme

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