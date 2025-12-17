package com.rollinup.rollinup.component.date

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import kotlinx.datetime.LocalDateTime

@Composable
fun DateText(
    dateString: String,
    format: DateTextFormat = DateTextFormat.DATE_TIME,
    color: Color = theme.bodyText,
    style: TextStyle = Style.body,
    showYear:Boolean = true
) {
    Text(
        text = DateFormatter.formateDateTimeFromString(dateString, format, showYear),
        color = color,
        style = style
    )
}

@Composable
fun DateText (
    dateTime: LocalDateTime,
    format: DateTextFormat = DateTextFormat.DATE_TIME,
    color : Color = theme.bodyText,
    style : TextStyle = Style.body,
    showYear: Boolean = true
) {
    Text(
        text = DateFormatter.formatDateTime(dateTime, format, showYear),
        color = color,
        style = style,
    )
}

@Composable
fun PermitDateText(
    start: String,
    end: String,
    type: PermitType,
    style: TextStyle = Style.body,
    color: Color = theme.bodyText,
) {
    Text(
        text = DateFormatter.formatPermitDateRange(
            type = type,
            start = start,
            end = end
        ),
        style = style,
        color = color
    )
}
