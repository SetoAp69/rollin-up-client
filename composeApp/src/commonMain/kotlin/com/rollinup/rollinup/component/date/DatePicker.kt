@file:OptIn(ExperimentalTime::class)

package com.rollinup.rollinup.component.date

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusDays
import com.kizitonwose.calendar.core.plusMonths
import com.rollinup.common.utils.Utils.toEpochMilli
import com.rollinup.common.utils.Utils.toFormattedString
import com.rollinup.common.utils.Utils.toLocalDate
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.button.ButtonType
import com.rollinup.rollinup.component.model.Platform
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextError
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.LocalHolidayProvider
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getScreenHeight
import com.rollinup.rollinup.component.utils.getScreenWidth
import com.rollinup.rollinup.component.utils.isCompact
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_close_line_24
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_left_24
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_right_24
import kotlin.time.ExperimentalTime

@Composable
fun DatePickerField(
    title: String,
    placeholder: String,
    value: List<Long>,
    maxSelection: Int = 3,
    color: DatePickerColor = DatePickerDefault.color,
    isError: Boolean = false,
    errorText: String? = null,
    enabled: Boolean = true,
    isDisablePastSelection: Boolean = true,
    platform: Platform,
    isRequired: Boolean = false,
    onValueChange: (List<Long>) -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    val selectedDate = value.map { it.toLocalDate() }
    val stringValue = when {
        selectedDate.isEmpty() -> placeholder
        selectedDate.size == 1 -> selectedDate.first().toFormattedString()
        else -> selectedDate.first().toFormattedString() + " - " + selectedDate.last()
            .toFormattedString()
    }

    val textColor = when {
        isError -> theme.danger
        selectedDate.isEmpty() -> theme.textPrimary.copy(alpha = 0.4f)
        else -> theme.textPrimary
    }

    val textStyle =
        if (selectedDate.isEmpty()) Style.body else Style.body.copy(fontWeight = FontWeight.W600)
    val lineColor = if (isError) theme.danger else theme.textPrimary
    val interactionSource = remember { MutableInteractionSource() }

    TextFieldTitle(
        text = title,
        isRequired = isRequired
    ) {
        Column {
            Column(
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = enabled
                    ) {
                        showBottomSheet = true
                    }
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = itemGap8)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringValue,
                    style = textStyle,
                    color = textColor
                )
                HorizontalDivider(color = lineColor, thickness = 1.dp)
            }
            Spacer(itemGap4)
            TextError(
                text = errorText ?: "",
                isError = isError
            )
        }
    }

    when (platform) {
        Platform.IOS, Platform.ANDROID -> {
            DatePickerBottomSheet(
                isShowSheet = showBottomSheet,
                onDismissRequest = { showBottomSheet = it },
                value = selectedDate,
                onValueChange = {
                    onValueChange(
                        it.map { date -> date.toEpochMilli() }
                            .sortedBy { millis -> millis }
                    )
                },
                isDisabledPastSelection = isDisablePastSelection,
                maxSelection = maxSelection,
                color = color
            )
        }

        else -> {
            DatePickerDialog(
                isShowDialog = showBottomSheet,
                onDismissRequest = { showBottomSheet = it },
                value = selectedDate,
                onValueChange = {
                    onValueChange(
                        it.map { date -> date.toEpochMilli() }
                            .sortedBy { millis -> millis }
                    )
                },
                isDisabledPastSelection = isDisablePastSelection,
                maxSelection = maxSelection,
                color = color
            )
        }
    }
}

@Composable
fun DatePickerDialog(
    isShowDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    value: List<LocalDate>,
    onValueChange: (List<LocalDate>) -> Unit,
    isDisabledPastSelection: Boolean = true,
    maxSelection: Int = Int.MAX_VALUE,
    color: DatePickerColor = DatePickerDefault.color,
) {
    val width = getScreenWidth() * 0.2f
    val height = getScreenHeight() * 0.56f
    var currentValue by remember { mutableStateOf(value) }

    LaunchedEffect(isShowDialog) {
        if (value != currentValue && isShowDialog) currentValue = value
    }

    if (isShowDialog) {
        Dialog(
            onDismissRequest = {
                onDismissRequest(false)
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Column(
                modifier = Modifier
                    .background(
                        shape = RoundedCornerShape(12.dp),
                        color = theme.popUpBg
                    )
                    .sizeIn(
                        minWidth = 360.dp,
                        minHeight = 526.dp,
                        maxWidth = if (width > 360.dp) width else 360.dp,
                        maxHeight = if (height > 526.dp) height else 526.dp
                    )
                    .padding(if (isCompact) 18.dp else 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                onDismissRequest(false)
                            }
                            .size(32.dp),
                        tint = theme.lineStroke,
                        painter = painterResource(Res.drawable.ic_close_line_24),
                        contentDescription = null
                    )
                }
                DatePickerCalendar(
                    value = currentValue,
                    onValueChange = { value ->
                        currentValue = value
                    },
                    isDisabledPastSelection = isDisabledPastSelection,
                    maxSelection = maxSelection,
                    color = color
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        text = "Reset",
                        type = ButtonType.OUTLINED,
                    ) {
                        currentValue = emptyList()
                    }
                    Spacer(itemGap8)
                    Button(
                        modifier = Modifier.weight(1f),
                        text = "Apply",
                    ) {
                        onValueChange(currentValue)
                        onDismissRequest(false)
                    }
                }
            }
        }
    }
}

@Composable
fun DatePickerBottomSheet(
    isShowSheet: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    value: List<LocalDate>,
    onValueChange: (List<LocalDate>) -> Unit,
    isDisabledPastSelection: Boolean = true,
    maxSelection: Int = Int.MAX_VALUE,
    color: DatePickerColor = DatePickerDefault.color,
) {
    var currentValue by remember { mutableStateOf(value) }

    LaunchedEffect(isShowSheet) {
        if (value != currentValue && isShowSheet) currentValue = value
    }

    BottomSheet(
        isShowSheet = isShowSheet,
        modifier = Modifier
            .height(getScreenHeight() * 0.52f),
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isCompact) 12.dp else 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(16.dp)
            DatePickerCalendar(
                value = currentValue,
                onValueChange = { value ->
                    currentValue = value
                },
                maxSelection = maxSelection,
                isDisabledPastSelection = isDisabledPastSelection,
                color = color
            )
            Spacer(modifier = Modifier.weight(1f))
            Row {
                Button(
                    modifier = Modifier.weight(1f),
                    text = "Reset",
                    type = ButtonType.OUTLINED,
                ) {
                    currentValue = emptyList()
                }
                Spacer(itemGap8)
                Button(
                    modifier = Modifier.weight(1f),
                    text = "Apply",
                ) {
                    onValueChange(currentValue)
                    onDismissRequest(false)
                }

            }
        }
    }

}

@Composable
fun DatePickerCalendar(
    value: List<LocalDate> = emptyList(),
    onValueChange: (List<LocalDate>) -> Unit,
    isDisabledPastSelection: Boolean = true,
    maxSelection: Int = Int.MAX_VALUE,
    color: DatePickerColor = DatePickerDefault.color,
) {
    val dayOfWeeks = remember { daysOfWeek() }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val holidayList = LocalHolidayProvider.current

    val calendarState = rememberCalendarState(
        startMonth = currentMonth,
        endMonth = currentMonth,
        firstVisibleMonth = currentMonth,
        outDateStyle = OutDateStyle.EndOfGrid,
    )

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        HorizontalCalendar(
            state = calendarState,
            userScrollEnabled = false,
            monthHeader = { month ->
                DatePickerMonthHeader(
                    month = month,
                    onNextMonth = {
                        currentMonth = currentMonth.plusMonths(1)
                    },
                    onPreviousMonth = {
                        currentMonth = currentMonth.minusMonths(1)
                    },
                    dayOfWeeks = dayOfWeeks
                )
            },
            modifier = Modifier.width(maxWidth),
            dayContent = { day ->
                DayContent(
                    selectedDate = value.sortedBy { it },
                    day = day,
                    isDisablePastSelection = isDisabledPastSelection,
                    onUpdateSelection = { date ->
                        when {
                            date in value -> {
                                onValueChange(value.minus(date))
                            }

                            value.size < 2 -> {
                                val first = value.firstOrNull()
                                val totalDaysInSelection =
                                    calculateSelectedDate(first, date, holidayList)

                                if (first != null && totalDaysInSelection > maxSelection) return@DayContent
                                onValueChange(value.plus(date))
                            }

                            else -> onValueChange(listOf(date))
                        }
                    },
                    color = color,
                    holidayList = holidayList,
                )
            }
        )
    }
}

@Composable
fun DayContent(
    selectedDate: List<LocalDate>,
    day: CalendarDay,
    isDisablePastSelection: Boolean,
    onUpdateSelection: (LocalDate) -> Unit,
    holidayList: List<LocalDate>,
    color: DatePickerColor,
) {
    val isInBetween =
        selectedDate.firstOrNull()?.let { day.date >= it } ?: false && selectedDate.lastOrNull()
            ?.let { day.date <= it } ?: false
    val isStart = day.date == selectedDate.firstOrNull()
    val isEnd = day.date == selectedDate.lastOrNull()
    val isPast = day.date < LocalDate.now()
    val isWeekend = day.date.dayOfWeek in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
    val isHoliday = holidayList.contains(day.date)

    val dateTextColor = when {
        isStart || isEnd -> color.selectedTextColor
        isInBetween -> color.textColor
        isPast && isDisablePastSelection -> color.disabledTextColor
        isWeekend -> color.weekendTextColor
        isHoliday -> color.holidayTextColor
        else -> color.textColor
    }

    if (day.position == DayPosition.MonthDate) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    enabled = filterActiveDate(
                        date = day.date,
                        isDisabledPastSelection = isDisablePastSelection,
                        holidayList = holidayList
                    )
                ) {
                    onUpdateSelection(day.date)
                }
                .datePickerHighlight(
                    isStart = isStart,
                    isEnd = isEnd,
                    isInBetween = isInBetween,
                    startEndColor = color.selectedHighlightColor,
                    inBetweenColor = color.selectedBackgroundColor
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(itemGap8),
                text = day.date.day.toString(),
                style = Style.body,
                color = dateTextColor
            )
        }
    }

}

@Composable
fun DatePickerMonthHeader(
    month: CalendarMonth,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit,
    dayOfWeeks: List<DayOfWeek>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = itemGap4),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_drop_down_arrow_line_left_24),
                tint = theme.bodyText,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onPreviousMonth()
                    }
            )
            Text(
                text = month.yearMonth.month.name + " " + month.yearMonth.year,
                style = Style.title,
                color = theme.bodyText
            )
            Icon(
                painter = painterResource(Res.drawable.ic_drop_down_arrow_line_right_24),
                tint = theme.bodyText,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onNextMonth()
                    }
            )
        }
        Spacer(itemGap8)
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            for (day in dayOfWeeks) {
                Text(
                    text = day.name.take(3),
                    style = Style.title,
                    color = theme.bodyText,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
private fun filterActiveDate(
    date: LocalDate,
    isDisabledPastSelection: Boolean,
    holidayList: List<LocalDate>,
): Boolean {
    val pastState =
        if (isDisabledPastSelection) date >= LocalDate.now() else true
    val activeState = isActiveDate(date, holidayList)
    return pastState && activeState
}

private fun calculateSelectedDate(
    from: LocalDate?,
    to: LocalDate?,
    holidayList: List<LocalDate>,
): Int {
    if (from == null || to == null) return 0

    val range =
        if (from > to) {
            generateSequence(from) {
                if (it > to) it.minusDays(1) else null
            }
        } else {
            generateSequence(from) {
                if (it < to) it.plusDays(1) else null
            }
        }
            .toList()
            .filter { isActiveDate(it, holidayList) }

    return range.size
}

object DatePickerDefault {
    val color: DatePickerColor
        @Composable
        get() = DatePickerColor(
            selectedHighlightColor = theme.primary,
            selectedBackgroundColor = theme.popUpBgSelected,
            selectedTextColor = theme.textBtnPrimary,
            disabledTextColor = theme.textFieldText,
            holidayTextColor = theme.danger50,
            weekendTextColor = theme.danger50,
            textColor = theme.bodyText,
            backgroundColor = Color.Transparent,
        )
}

private fun isActiveDate(
    date: LocalDate,
    holidayList: List<LocalDate>,
): Boolean {
    val isHoliday = date in holidayList
    val isWeekend = date.dayOfWeek in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

    return !isHoliday && !isWeekend
}

private fun Modifier.datePickerHighlight(
    isStart: Boolean,
    isEnd: Boolean,
    isInBetween: Boolean,
    startEndColor: Color,
    inBetweenColor: Color,
): Modifier = composed {
    when {
        isStart && isEnd -> {
            background(
                shape = CircleShape,
                color = startEndColor
            )
        }

        isStart -> {
            background(
                shape = RoundedCornerShape(topStartPercent = 50, bottomStartPercent = 50),
                color = inBetweenColor
            )
                .background(
                    shape = CircleShape,
                    color = startEndColor
                )
        }

        isEnd -> {
            background(
                shape = RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50),
                color = inBetweenColor
            )
                .background(
                    shape = CircleShape,
                    color = startEndColor
                )
        }

        isInBetween -> {
            background(
                shape = RectangleShape,
                color = inBetweenColor
            )
        }

        else -> this
    }
}
