package com.rollinup.rollinup.component.date

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import kotlinx.datetime.LocalDateTime

/**
 * Displays a formatted date string using the application's standard date formatter.
 *
 * @param dateString The raw date string to be formatted.
 * @param format The target format for the display (default is Date & Time).
 * @param color The text color.
 * @param style The typography style.
 * @param showYear Whether to include the year in the formatted output.
 */
@Composable
fun DateText(
    dateString: String,
    format: DateTextFormat = DateTextFormat.DATE_TIME,
    color: Color = theme.bodyText,
    style: TextStyle = Style.body,
    showYear: Boolean = true,
) {
    Text(
        text = DateFormatter.formateDateTimeFromString(dateString, format, showYear),
        color = color,
        style = style
    )
}

/**
 * Displays a formatted date from a [LocalDateTime] object.
 *
 * @param dateTime The LocalDateTime object to format.
 * @param format The target format for the display.
 * @param color The text color.
 * @param style The typography style.
 * @param showYear Whether to include the year in the formatted output.
 */
@Composable
fun DateText(
    dateTime: LocalDateTime,
    format: DateTextFormat = DateTextFormat.DATE_TIME,
    color: Color = theme.bodyText,
    style: TextStyle = Style.body,
    showYear: Boolean = true,
) {
    Text(
        text = DateFormatter.formatDateTime(dateTime, format, showYear),
        color = color,
        style = style,
    )
}

/**
 * A specialized text component for displaying a permit's validity period.
 *
 * Formats the start and end dates according to the specific rules of the [PermitType].
 *
 * @param start The start date string.
 * @param end The end date string.
 * @param type The type of permit, which dictates the formatting logic.
 * @param style The typography style.
 * @param color The text color.
 */
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