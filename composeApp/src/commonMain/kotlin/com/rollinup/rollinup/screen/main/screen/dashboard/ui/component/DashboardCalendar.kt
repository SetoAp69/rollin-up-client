package com.rollinup.rollinup.screen.dashboard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.now
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.LocalHolidayProvider
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minusMonth
import kotlinx.datetime.plusMonth
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_left_24
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_right_24
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun DashboardCalendar(
    attendanceList: List<AttendanceByStudentEntity>,
    isLoading: Boolean,
    title: String,
    onClickDay: (AttendanceByStudentEntity?) -> Unit,
) {

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysOfWeek = remember { daysOfWeek() }
    val calendarState = rememberCalendarState(
        startMonth = currentMonth,
        endMonth = currentMonth,
    )

    TextFieldTitle(
        title = title,
        textStyle = Style.label
    ) {
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalCalendar(
                modifier = Modifier
                    .width(maxWidth),
                state = calendarState,
                calendarScrollPaged = false,
                userScrollEnabled = false,
                reverseLayout = false,
                monthFooter = { _ ->
                    Spacer(itemGap8)
                },
                dayContent = {
                    DayContent(
                        day = it,
                        attendanceEntity = attendanceList.find { attendanceByStudentEntity ->
                            attendanceByStudentEntity.localDate == it.date
                        },
                        onClick = onClickDay,
                        isLoading = isLoading
                    )
                },
                monthHeader = {
                    MonthHeader(
                        onChangesMonth = { newMonth ->
                            currentMonth = newMonth
                        },
                        dayOfWeeks = daysOfWeek,
                        month = it
                    )
                },
                monthContainer = { _, content ->
                    Box(
                        modifier = Modifier
                            .width(maxWidth)
                            .background(
                                color = theme.popUpBg,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = theme.primary,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        content()
                    }
                }
            )
        }
    }
}

@Composable
fun MonthHeader(
    onChangesMonth: (YearMonth) -> Unit,
    dayOfWeeks: List<DayOfWeek>,
    month: CalendarMonth,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp))
            .background(color = theme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .clickable {
                            onChangesMonth(month.yearMonth.minusMonth())
                        }
                        .size(16.dp),
                    tint = theme.textBtnPrimary,
                    painter = painterResource(Res.drawable.ic_drop_down_arrow_line_left_24),
                    contentDescription = "Previous month"
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = month.yearMonth.month.name + " " + month.yearMonth.year,
                    textAlign = TextAlign.Center,
                    style = Style.title,
                    color = theme.textBtnPrimary
                )
                Icon(
                    modifier = Modifier
                        .clickable {
                            onChangesMonth(month.yearMonth.plusMonth())
                        }
                        .size(16.dp),
                    tint = theme.textBtnPrimary,
                    painter = painterResource(Res.drawable.ic_drop_down_arrow_line_right_24),
                    contentDescription = "Next month"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (day in dayOfWeeks) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = day.name.take(3),
                        textAlign = TextAlign.Center,
                        style = Style.title,
                        color = theme.textBtnPrimary
                    )
                }
            }
        }
    }
}


@Composable
private fun DayContent(
    day: CalendarDay,
    isLoading: Boolean,
    attendanceEntity: AttendanceByStudentEntity?,
    onClick: (AttendanceByStudentEntity?) -> Unit,
) {
    if (day.position == DayPosition.MonthDate) {
        val isHoliday = LocalHolidayProvider.current.contains(day.date)
        val isWeekend = day.date.dayOfWeek in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (isHoliday || isWeekend) return@clickable
                    onClick(attendanceEntity)
                },
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(itemGap4),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = day.date.day.toString(),
                    style = Style.header,
                    color = getDaysColor(
                        date = day.date,
                        isHoliday = isHoliday
                    )
                )
                Spacer(itemGap4)
                if (!(isHoliday || isWeekend)) {
                    if (isLoading) {
                        ShimmerEffect(itemGap4, itemGap4)
                    } else {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    color = getStatusColor(
                                        attendanceStatus =
                                            attendanceEntity?.status
                                                ?: AttendanceStatus.NO_DATA
                                    ),
                                    shape = RoundedCornerShape(50)
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun getStatusColor(
    attendanceStatus: AttendanceStatus,
): Color {
    return when (attendanceStatus) {
        AttendanceStatus.ON_TIME -> theme.primary
        AttendanceStatus.LATE, AttendanceStatus.EXCUSED, AttendanceStatus.APPROVAL_PENDING -> theme.warning
        AttendanceStatus.ABSENT -> theme.danger
        AttendanceStatus.NO_DATA -> theme.chipDisabledBg
    }
}

@Composable
private fun getDaysColor(
    date: LocalDate,
    isHoliday: Boolean,
): Color {
    return when {
        isHoliday || date.dayOfWeek == DayOfWeek.SUNDAY -> theme.danger
        date.dayOfWeek == DayOfWeek.SATURDAY -> theme.chipDisabledBg
        else -> theme.bodyText
    }
}
