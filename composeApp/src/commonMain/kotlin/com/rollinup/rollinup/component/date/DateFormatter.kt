package com.rollinup.rollinup.component.date

import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.utils.Utils.now
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone

object DateFormatter {
    private val dayShort = hashMapOf(
        1 to "Mon", 2 to "Tue", 3 to "Wed", 4 to "Thu", 5 to "Fri", 6 to "Sat", 7 to "Sun"
    )
    private val monthShort = hashMapOf(
        1 to "Jan", 2 to "Feb", 3 to "Mar", 4 to "Apr", 5 to "May", 6 to "Jun",
        7 to "Jul", 8 to "Aug", 9 to "Sep", 10 to "Oct", 11 to "Nov", 12 to "Dec"
    )

    private fun LocalDate.dayOfWeekShort(): String {
        return dayShort[this.dayOfWeek.ordinal + 1] ?: ""
    }

    fun formateDateTimeFromString(
        dateTime: String,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): String {
        val dt = dateTime.parseToLocalDateTime(timeZone)
        return formatDateTime(dt)
    }

    fun formatDateShort(date: LocalDate, showYear: Boolean = false): String {
        val day = date.dayOfWeekShort()
        val month = monthShort[date.month.ordinal + 1] ?: ""
        return "$day, ${date.day} $month ${if (showYear) date.year else ""}".trim()
    }

    fun formatDateTime(dt: LocalDateTime): String {
        return "${formatDateShort(dt.date)} ${formatTime(dt.time)}"
    }

    private fun pad2(v: Int) = v.toString().padStart(2, '0')

    fun formatTime(localTime: LocalTime): String {
        return "${pad2(localTime.hour)}:${pad2(localTime.minute)}"
    }

    fun formatTimeMinuteSecond(localTime: LocalTime): String {
        return "${pad2(localTime.minute)}:${pad2(localTime.second)}"
    }


    fun formatTimeRange(start: LocalTime, end: LocalTime): String {
        return "${formatTime(start)} - ${formatTime(end)}"
    }

    fun formatDateRange(start: LocalDate, end: LocalDate): String {
        return if (start == end) {
            formatDateShort(start)
        } else {
            "${formatDateShort(start)} - ${formatDateShort(end)}"
        }
    }

    fun formateDateTimeFromString(
        dateString: String,
        format: DateTextFormat,
        showYear: Boolean = true,
    ): String {
        val dateTime = dateString.parseToLocalDateTime()
        val date = dateTime.date
        val time = dateTime.time

        return when (format) {
            DateTextFormat.DATE -> formatDateShort(date, showYear)
            DateTextFormat.DATE_TIME -> formatDateTime(dateTime)
            DateTextFormat.TIME -> formatTime(time)
        }
    }

    fun formatDateTime(
        dt: LocalDateTime,
        format: DateTextFormat,
        showYear: Boolean = true,
    ): String {
        val date = dt.date
        val time = dt.time

        return when (format) {
            DateTextFormat.DATE -> formatDateShort(date, showYear)
            DateTextFormat.DATE_TIME -> formatDateTime(dt)
            DateTextFormat.TIME -> formatTime(time)
        }
    }

    fun formatPermitDateRange(
        type: PermitType,
        start: String,
        end: String,
    ): String {
        val dtStart = start.parseToLocalDateTime()
        val dtEnd = end.parseToLocalDateTime()

        return when (type) {
            PermitType.DISPENSATION -> {
                formatTimeRange(dtStart.time, dtEnd.time)
            }

            PermitType.ABSENCE -> {
                if (dtStart.date == dtEnd.date) {
                    formatDateShort(dtStart.date)
                } else {
                    formatDateRange(dtStart.date, dtEnd.date)
                }
            }
        }
    }
}

