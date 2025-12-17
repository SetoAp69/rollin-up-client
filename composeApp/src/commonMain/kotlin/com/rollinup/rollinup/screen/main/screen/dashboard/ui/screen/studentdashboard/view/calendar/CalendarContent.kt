package com.rollinup.rollinup.screen.dashboard.ui.screen.studentdashboard.view.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.michaelflisar.lumberjack.core.L
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.dashboard.model.studentdashboard.StudentDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.uistate.StudentDashboardUiState

@Composable
fun CalendarContent(
    uiState: StudentDashboardUiState,
    cb: StudentDashboardCallback,
) {
    val state = rememberCalendarState()
    HorizontalCalendar(
        state = state ,
        calendarScrollPaged = true ,
        userScrollEnabled = true,
        reverseLayout = false ,
        modifier = Modifier.fillMaxWidth(),
//        contentPadding = ,
//        contentHeightMode = ,
        dayContent ={
            Text(
                text = it.date.day.toString(),
                style = Style.title,
                color = theme.bodyText
            )
        } ,
        monthHeader ={
            Text(
                text = it.yearMonth.month.name,
                style = Style.header,
                color = theme.bodyText
            )
        } ,
        monthBody = { month, content->

            LaunchedEffect(month){
                L.wtf { "moth changes: $month" }
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(theme.primary)) {
                content()
            }
        },
        monthFooter = {},
        monthContainer ={ month,content->
            LaunchedEffect(month){
                L.w {
                    "month changes from container: $month"
                }
            }
            content()
//            month.weekDays.flatMap { it }.forEach {
//                L.w {
//                    "day(s) from container: $it"
//                }
//                Box(){
//                    content()
//                }
//                Text(
//                    text = it.date.day.toString(),
//                    style = Style.title,
//                    color = theme.bodyText
//                )
//            }

        }
    )
}
