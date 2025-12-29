@file:OptIn(ExperimentalTime::class)

package com.rollinup.rollinup.component.date

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.common.utils.Utils.toEpochMilli
import com.rollinup.common.utils.Utils.toFormattedString
import com.rollinup.common.utils.Utils.toLocalDate
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.dropdown.DropDownMenuItem
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextError
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.LocalHolidayProvider
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_left_24
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_right_24
import rollin_up.composeapp.generated.resources.label_date
import kotlin.time.ExperimentalTime

/**
 * A composable that provides a text field for selecting a single date from a date picker dialog.
 *
 * @param title The title of the date picker field.
 * @param value The currently selected date in milliseconds since epoch.
 * @param enable Whether the date picker field is enabled.
 * @param contentColor The color of the content (text and icon).
 * @param backgroundColor The background color of the date picker field.
 * @param placeHolder The placeholder text to display when no date is selected.
 * @param width The width of the date picker field.
 * @param isError Whether the date picker field is in an error state.
 * @param textError The error text to display when in an error state.
 * @param onValueChange A callback that is invoked when a date is selected.
 * @param isDisablePastSelection Whether to disable the selection of past dates.
 * @param isAllSelectable Whether all dates are selectable.
 * @param color The color scheme for the date picker.
 */
@Composable
fun SingleDatePickerField(
    title: String,
    value: Long?,
    enable: Boolean = true,
    contentColor: Color = theme.textPrimary,
    backgroundColor: Color = theme.secondary,
    placeHolder: String = "-",
    width: Dp? = 150.dp,
    isError: Boolean = false,
    textError: String? = null,
    onValueChange: (Long?) -> Unit,
    isDisablePastSelection: Boolean = false,
    isAllSelectable: Boolean = false,
    color: DatePickerColor = DatePickerDefault.color,
) {
    var showPicker by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (showPicker) 90f else 0F)
    val sValue = value?.parseToLocalDateTime()?.date?.toString() ?: placeHolder

    val modifier = width?.let {
        Modifier.width(it)
    } ?: Modifier.fillMaxWidth()

    val contentColor = if (isError) theme.danger else contentColor
    val backgroundColor = if (isError) theme.textFieldBgError else backgroundColor

    TextFieldTitle(
        title = title,
    ) {
        Row(
            modifier = Modifier
                .clickable(enable) {
                    showPicker = true
                }
                .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
                .then(modifier)
                .padding(itemGap4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(itemGap4),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_drop_down_arrow_line_right_24),
                tint = contentColor,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(rotationState),
                contentDescription = null
            )
            Text(
                text = sValue,
                style = Style.title,
                color = contentColor
            )
        }
        TextError(
            text = textError ?: "",
            isError = isError
        )
    }
    SingleDatePicker(
        isShowDatePicker = showPicker,
        onDismissRequest = { showPicker = it },
        value = value?.toLocalDate(),
        onSelectDate = { date ->
            onValueChange(date?.toEpochMilli())
        },
        isDisablePastSelection = isDisablePastSelection,
        isAllSelectable = isAllSelectable,
        color = color
    )
}

/**
 * A composable that provides a filterable date picker for selecting a single date.
 *
 * @param title The title of the date picker.
 * @param value The currently selected date in milliseconds since epoch.
 * @param enabled Whether the date picker is enabled.
 * @param placeHolder The placeholder text to display when no date is selected.
 * @param onValueChange A callback that is invoked when a date is selected.
 */
@Composable
fun SingleFilterDatePicker(
    title: String,
    value: Long?,
    enabled: Boolean,
    placeHolder: String = stringResource(Res.string.label_date),
    onValueChange: (Long?) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val rotationValue by animateFloatAsState(if (showDatePicker) 90f else 0f)

    val dateValue = value?.toLocalDate()
    val label = when {
        dateValue != null -> dateValue.toFormattedString()
        else -> placeHolder
    }

    TextFieldTitle(
        title = title,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(enabled) { showDatePicker = true }
                .background(
                    color = theme.secondary,
                    shape = RoundedCornerShape(8.dp),
                )
                .fillMaxWidth()
                .padding(itemGap4)
        ) {
            if (enabled) {
                Icon(
                    painter = painterResource(Res.drawable.ic_drop_down_arrow_line_right_24),
                    tint = theme.primary,
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(rotationValue)
                )
                Spacer(itemGap4)
            }
            Text(
                text = label,
                color = theme.primary,
                style = Style.body
            )
        }
    }

    SingleDatePicker(
        isShowDatePicker = showDatePicker,
        onDismissRequest = { showDatePicker = it },
        value = value?.toLocalDate(),
        onSelectDate = { date -> onValueChange(date?.toEpochMilli()) },
        isDisablePastSelection = false,
        isAllSelectable = true,
    )
}

/**
 * A composable that provides a filterable date picker for selecting a range of dates.
 *
 * @param title The title of the date picker.
 * @param value The currently selected date range in milliseconds since epoch.
 * @param enabled Whether the date picker is enabled.
 * @param placeHolder The placeholder text to display when no date is selected.
 * @param onValueChange A callback that is invoked when a date range is selected.
 */
@Composable
fun FilterDatePicker(
    title: String,
    value: List<Long>,
    enabled: Boolean,
    placeHolder: String = stringResource(Res.string.label_date),
    onValueChange: (List<Long>) -> Unit,
) {
    var showDateSelector by remember { mutableStateOf(false) }
    val rotationValue by animateFloatAsState(if (showDateSelector) 90f else 0f)

    val dateValue = value.map { it.toLocalDate() }
    val label = when {
        dateValue.size == 1 -> dateValue.first().toFormattedString()
        dateValue.isEmpty() -> placeHolder
        else -> {
            val from = dateValue.first().month.name.take(3) + " " + dateValue.first().day.toString()
            val to = dateValue.last().month.name.take(3) + " " + dateValue.last().day.toString()

            "$from - $to"
        }
    }

    TextFieldTitle(
        title = title,
        modifier = Modifier.padding(vertical = itemGap4)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(enabled) {
                    showDateSelector = true
                }
                .background(
                    color = theme.secondary,
                    shape = RoundedCornerShape(8.dp),
                )
                .fillMaxWidth()
                .padding(itemGap4)
        ) {
            if (enabled) {
                Icon(
                    painter = painterResource(Res.drawable.ic_drop_down_arrow_line_right_24),
                    tint = theme.primary,
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(rotationValue)
                )
                Spacer(itemGap4)
            }
            Text(
                text = label,
                color = theme.textPrimary,
                style = Style.title
            )
        }
    }
    DateRangePicker(
        isShowDatePicker = showDateSelector,
        onDismissRequest = { showDateSelector = it },
        value = value.map { it.toLocalDate() },
        onSelectDate = { value -> onValueChange(value.map { it.toEpochMilli() }) },
    )
}

/**
 * A composable that provides a text field for selecting a single date from a date picker dialog.
 * This is a convenience wrapper around [DateRangePickerField] with a `maxRange` of 1.
 *
 * @param title The title of the date picker field.
 * @param placeholder The placeholder text to display when no date is selected.
 * @param value The currently selected date in milliseconds since epoch.
 * @param color The color scheme for the date picker.
 * @param isError Whether the date picker field is in an error state.
 * @param isAllSelectable Whether all dates are selectable.
 * @param errorText The error text to display when in an error state.
 * @param enabled Whether the date picker field is enabled.
 * @param isDisablePastSelection Whether to disable the selection of past dates.
 * @param isRequired Whether the field is required.
 * @param onValueChange A callback that is invoked when a date is selected.
 */
@Composable
fun SingleDatePickerField(
    title: String,
    placeholder: String,
    value: Long?,
    color: DatePickerColor = DatePickerDefault.color,
    isError: Boolean = false,
    isAllSelectable: Boolean = false,
    errorText: String? = null,
    enabled: Boolean = true,
    isDisablePastSelection: Boolean = true,
    isRequired: Boolean = false,
    onValueChange: (Long?) -> Unit,
) {
    DateRangePickerField(
        title = title,
        placeholder = placeholder,
        value = value?.let { listOf(it) } ?: emptyList(),
        maxRange = 1,
        color = color,
        isError = isError,
        isAllSelectable = isAllSelectable,
        errorText = errorText,
        enabled = enabled,
        isDisablePastSelection = isDisablePastSelection,
        isRequired = isRequired
    ) {
        onValueChange(it.firstOrNull())
    }
}

/**
 * A composable that provides a text field for selecting a date range from a date picker dialog.
 *
 * @param title The title of the date picker field.
 * @param placeholder The placeholder text to display when no date is selected.
 * @param value The currently selected date range in milliseconds since epoch.
 * @param maxRange The maximum number of days that can be selected in a range.
 * @param color The color scheme for the date picker.
 * @param isError Whether the date picker field is in an error state.
 * @param isAllSelectable Whether all dates are selectable.
 * @param errorText The error text to display when in an error state.
 * @param enabled Whether the date picker field is enabled.
 * @param isDisablePastSelection Whether to disable the selection of past dates.
 * @param isRequired Whether the field is required.
 * @param onValueChange A callback that is invoked when a date range is selected.
 */
@Composable
fun DateRangePickerField(
    title: String,
    placeholder: String,
    value: List<Long>,
    maxRange: Int = 3,
    color: DatePickerColor = DatePickerDefault.color,
    isError: Boolean = false,
    isAllSelectable: Boolean = false,
    errorText: String? = null,
    enabled: Boolean = true,
    isDisablePastSelection: Boolean = true,
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

    Box{
        TextFieldTitle(
            title = title,
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

        DateRangePicker(
            isShowDatePicker = showBottomSheet,
            onDismissRequest = { showBottomSheet = it },
            value = value.map { it.toLocalDate() },
            onSelectDate = { value -> onValueChange(value.map { it.toEpochMilli() }) },
            maxRange = maxRange,
            isDisablePastSelection = isDisablePastSelection,
            isAllSelectable = isAllSelectable,
            color = color
        )
    }

}

/**
 * An expect composable for displaying a single-date picker.
 *
 * @param isShowDatePicker Whether to show the date picker.
 * @param onDismissRequest A callback that is invoked when the date picker is dismissed.
 * @param value The currently selected date.
 * @param onSelectDate A callback that is invoked when a date is selected.
 * @param isDisablePastSelection Whether to disable the selection of past dates.
 * @param isAllSelectable Whether all dates are selectable.
 * @param color The color scheme for the date picker.
 */
@Composable
expect fun SingleDatePicker(
    isShowDatePicker: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    value: LocalDate?,
    onSelectDate: (LocalDate?) -> Unit,
    isDisablePastSelection: Boolean = false,
    isAllSelectable: Boolean = true,
    color: DatePickerColor = DatePickerDefault.color,
)

/**
 * An expect composable for displaying a date range picker.
 *
 * @param isShowDatePicker Whether to show the date picker.
 * @param onDismissRequest A callback that is invoked when the date picker is dismissed.
 * @param value The currently selected date range.
 * @param onSelectDate A callback that is invoked when a date range is selected.
 * @param maxRange The maximum number of days that can be selected in a range.
 * @param isDisablePastSelection Whether to disable the selection of past dates.
 * @param isAllSelectable Whether all dates are selectable.
 * @param color The color scheme for the date picker.
 */
@Composable
expect fun DateRangePicker(
    isShowDatePicker: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    value: List<LocalDate>,
    onSelectDate: (List<LocalDate>) -> Unit,
    title: String = "",
    maxRange: Int = Int.MAX_VALUE,
    isDisablePastSelection: Boolean = false,
    isAllSelectable: Boolean = true,
    color: DatePickerColor = DatePickerDefault.color,
)

/**
 * A composable that displays a calendar for selecting a single date.
 *
 * @param value The currently selected date.
 * @param onSelectDate A callback that is invoked when a date is selected.
 * @param isAllSelectable Whether all dates are selectable.
 * @param isDisablePastSelection Whether to disable the selection of past dates.
 * @param color The color scheme for the date picker.
 */
@Composable
fun DatePickerCalendar(
    value: LocalDate?,
    onSelectDate: (LocalDate) -> Unit,
    isAllSelectable: Boolean = true,
    isDisablePastSelection: Boolean = false,
    color: DatePickerColor = DatePickerDefault.color,
) {
    val holidayList = LocalHolidayProvider.current

    BaseDatePickerCalendar(
        holidayList = holidayList,
        value = value?.let { listOf(it) } ?: emptyList(),
        onSelectDate = onSelectDate,
        isAllSelectable = isAllSelectable,
        isDisablePastSelection = isDisablePastSelection,
        color = color
    )
}

/**
 * A composable that displays a calendar for selecting a date range.
 *
 * @param value The currently selected date range.
 * @param onSelectDate A callback that is invoked when a date range is selected.
 * @param isDisablePastSelection Whether to disable the selection of past dates.
 * @param isAllSelectable Whether all dates are selectable.
 * @param maxSelection The maximum number of days that can be selected in a range.
 * @param color The color scheme for the date picker.
 */
@Composable
fun DateRangePickerCalendar(
    value: List<LocalDate> = emptyList(),
    onSelectDate: (List<LocalDate>) -> Unit,
    isDisablePastSelection: Boolean = true,
    isAllSelectable: Boolean,
    maxSelection: Int = Int.MAX_VALUE,
    color: DatePickerColor = DatePickerDefault.color,
) {
    val holidayList = LocalHolidayProvider.current

    BaseDatePickerCalendar(
        holidayList = holidayList,
        value = value,
        onSelectDate = { date ->
            when {
                date in value -> {
                    onSelectDate(value.minus(date))
                }

                value.size < 2 -> {
                    val first = value.firstOrNull()
                    val totalDaysInSelection =
                        calculateSelectedDate(first, date, holidayList)

                    if (first != null && totalDaysInSelection > maxSelection) return@BaseDatePickerCalendar
                    onSelectDate(value.plus(date))
                }

                else -> onSelectDate(listOf(date))
            }
        },
        isAllSelectable = isAllSelectable,
        isDisablePastSelection = isDisablePastSelection,
        color = color
    )
}

@Composable
private fun BaseDatePickerCalendar(
    holidayList: List<LocalDate>,
    value: List<LocalDate>,
    onSelectDate: (LocalDate) -> Unit,
    isAllSelectable: Boolean,
    isDisablePastSelection: Boolean,
    color: DatePickerColor,
) {
    val dayOfWeeks = remember { daysOfWeek() }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
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
                CalendarMonthHeader(
                    month = month,
                    onNextMonth = {
                        currentMonth = currentMonth.plusMonths(1)
                    },
                    onPreviousMonth = {
                        currentMonth = currentMonth.minusMonths(1)
                    },
                    dayOfWeeks = dayOfWeeks,
                    onYearSelected = {
                        currentMonth = YearMonth(it, month.yearMonth.month)
                    },
                    onMonthSelected = {
                        currentMonth = YearMonth(month.yearMonth.year, it)
                    }
                )
            },
            modifier = Modifier.width(maxWidth),
            dayContent = { day ->
                DayContent(
                    selectedDate = value.sortedBy { it },
                    day = day,
                    isDisablePastSelection = isDisablePastSelection,
                    onSelectDate = onSelectDate,
                    color = color,
                    holidayList = holidayList,
                    isAllSelectable = isAllSelectable
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
    isAllSelectable: Boolean,
    onSelectDate: (LocalDate) -> Unit,
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
                    enabled = isAllSelectable || filterActiveDate(
                        date = day.date,
                        isDisabledPastSelection = isDisablePastSelection,
                        holidayList = holidayList
                    )
                ) {
                    onSelectDate(day.date)
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
private fun CalendarMonthHeader(
    month: CalendarMonth,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit,
    dayOfWeeks: List<DayOfWeek>,
    onYearSelected: (Int) -> Unit = {},
    onMonthSelected: (Month) -> Unit = {},
) {
    val currentYear = month.yearMonth.year
    val yearRange = (currentYear - 50)..(currentYear + 10) // customize

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
                    .clickable { onPreviousMonth() }
            )

            Box(
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    MonthPicker(
                        currentMonth = month.yearMonth.month,
                        onValueChange = onMonthSelected
                    )
                    Spacer(itemGap4)
                    YearPicker(
                        currentYear = month.yearMonth.year,
                        yearRange = yearRange.toList(),
                        onValueChange = onYearSelected
                    )
                }
            }

            Icon(
                painter = painterResource(Res.drawable.ic_drop_down_arrow_line_right_24),
                tint = theme.bodyText,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onNextMonth() }
            )
        }

        Spacer(itemGap8)

        Row(modifier = Modifier.fillMaxWidth()) {
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
private fun YearPicker(
    currentYear: Int,
    yearRange: List<Int>,
    onValueChange: (Int) -> Unit,
) {
    var showDropdown by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(showDropdown) {
        if (showDropdown) {
            listState.scrollToItem(yearRange.indexOf(currentYear))
        }
    }

    Box {
        Text(
            text = "$currentYear",
            style = Style.title,
            color = theme.bodyText,
            modifier = Modifier
                .clickable {
                    showDropdown = true
                }
        )
        DropDownMenu(
            isShowDropDown = showDropdown,
            onDismissRequest = { showDropdown = false },
            modifier = Modifier.height(250.dp)
        ) {
            Box(modifier = Modifier.size(height = 250.dp, width = 100.dp)) {
                LazyColumn(
                    modifier = Modifier.height(250.dp),
                    state = listState
                ) {
                    items(yearRange.toList()) { year ->
                        DropDownMenuItem(
                            label = "$year",
                            backgroundColor = if (year == currentYear) theme.popUpBgSelected else Color.Transparent,
                            onClick = {
                                showDropdown = false
                                onValueChange(year)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthPicker(
    currentMonth: Month,
    onValueChange: (Month) -> Unit,
) {
    var showDropdown by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val monthList = Month.entries.toList()

    LaunchedEffect(showDropdown) {
        if (showDropdown) {
            listState.scrollToItem(monthList.indexOf(currentMonth))
        }
    }

    Box {
        Text(
            text = currentMonth.name,
            style = Style.title,
            color = theme.bodyText,
            modifier = Modifier
                .clickable {
                    showDropdown = true
                }
        )
        DropDownMenu(
            isShowDropDown = showDropdown,
            onDismissRequest = { showDropdown = false },
            modifier = Modifier.height(250.dp)
        ) {
            Box(modifier = Modifier.size(height = 250.dp, width = 100.dp)) {
                LazyColumn(
                    modifier = Modifier.height(250.dp),
                    state = listState
                ) {
                    items(monthList.toList()) { month ->
                        DropDownMenuItem(
                            label = "$month",
                            backgroundColor = if (month == currentMonth) theme.popUpBgSelected else Color.Transparent,
                            onClick = {
                                showDropdown = false
                                onValueChange(month)
                            }
                        )
                    }
                }
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
