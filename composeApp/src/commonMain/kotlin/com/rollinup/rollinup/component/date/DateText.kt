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
    format: DateTextFormat = DateTextFormat.DATE_TIME,
    color: Color = theme.bodyText,
    style: TextStyle = Style.body,
) {
    Text(
        text = formatDate(dateString, format),
        color = color,
        style = style
    )
}

fun formatDate(dateString: String, format: DateTextFormat): String {
    val dateTime = dateString.toLocalDateTime()
    val day = dateTime.dayOfWeek.name.take(3)
    val date = dateTime.day.toString()
    val month = dateTime.month.name.take(3)
    val year = dateTime.year.toString()
    val hour = dateTime.hour.toString()
    val minute = dateTime.minute.toString()

    return when (format) {
        DateTextFormat.DATE -> {
            "$day, $date $month $year"
        }

        DateTextFormat.DATE_TIME -> {
            "$day, $date $month $year $hour:$minute"
        }

        DateTextFormat.TIME -> {
            "$hour:$minute"
        }
    }
}
