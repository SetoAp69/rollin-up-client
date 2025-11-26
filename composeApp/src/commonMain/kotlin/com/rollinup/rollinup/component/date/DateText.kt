package com.rollinup.rollinup.component.date

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.rollinup.common.utils.Utils.toLocalDateTime
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme

@Composable
fun DateText(
    dateString: String,
    showTime: Boolean = true,
    color: Color = theme.bodyText,
    style: TextStyle = Style.body,
) {
    Text(
        text = formatDate(dateString, showTime),
        color = color,
        style = style
    )
}

fun formatDate(dateString: String, showTime: Boolean): String {
    val dateTime = dateString.toLocalDateTime()
    val day = dateTime.dayOfWeek.name.take(3)
    val date = dateTime.day.toString()
    val month = dateTime.month.name.take(3)
    val year = dateTime.year.toString()

    if (!showTime) {
        return "$day, $date $month $year"
    } else {
        val hour = dateTime.hour.toString()
        val minute = dateTime.minute.toString()
        return "$day, $date $month $year $hour:$minute"
    }
}