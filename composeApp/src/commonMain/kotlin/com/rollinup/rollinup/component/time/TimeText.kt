package com.rollinup.rollinup.component.time

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import kotlinx.datetime.LocalTime

@Composable
fun TimeText(
    value: LocalTime,
    showHour: Boolean = false,
    color: Color = theme.textPrimary,
    style: TextStyle = Style.title,
) {
    val h = "${value.hour}".padStart(2, '0')
    val m = "${value.minute}".padStart(2, '0')
    val s = "${value.second}".padStart(2, '0')

    val timeString = if (showHour) "$h:$m:$s" else "$m:$s"

    Text(
        text = timeString,
        color = color,
        style = style
    )
}