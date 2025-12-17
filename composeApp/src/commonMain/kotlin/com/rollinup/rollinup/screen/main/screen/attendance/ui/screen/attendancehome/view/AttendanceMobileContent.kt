package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.attendancedetail.AttendanceDetailDialog
import com.rollinup.rollinup.component.card.Card
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.date.SingleDatePickerField
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceCallback
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceHomeAction
import com.rollinup.rollinup.screen.main.screen.attendance.ui.navigation.AttendanceRoute
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.uistate.AttendanceUiState
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view.paging.AttendancePaging

@Composable
fun AttendanceMobileContent(
    uiState: AttendanceUiState,
    cb: AttendanceCallback,
    pagingData: LazyPagingItems<AttendanceByClassEntity>,
    onNavigateTo: (String) -> Unit,
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            AttendanceHomeTopBar(
                uiState = uiState,
                cb = cb,
                onNavigateUp = onNavigateUp
            )
        },
        showLoadingOverlay = uiState.isLoadingOverlay
    ) {
        Column {
            AttendanceMobileContentHeader(
                uiState = uiState,
                cb = cb
            )
            HorizontalDivider(
                modifier = Modifier.padding(screenPadding),
                color = theme.lineStroke,
                thickness = 1.dp
            )
            AttendanceMobileContent(
                uiState = uiState,
                cb = cb,
                pagingData = pagingData,
                onNavigateTo = onNavigateTo,
            )
        }
    }
}

@Composable
fun AttendanceMobileContentHeader(
    uiState: AttendanceUiState,
    cb: AttendanceCallback,
) {
    Box(modifier = Modifier.padding(horizontal = screenPadding)) {
        Card() {
            Column(modifier = Modifier.fillMaxWidth()) {
                SelectorSection(
                    uiState = uiState,
                    cb = cb
                )
                Spacer(12.dp)
                AttendanceSummary(
                    isLoading = uiState.isLoadingSummary,
                    summary = uiState.summary
                )
            }
        }
    }
}

@Composable
private fun SelectorSection(
    uiState: AttendanceUiState,
    cb: AttendanceCallback,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.width(150.dp)) {
            SingleDatePickerField(
                title = "",
                placeHolder = "",
                value = uiState.filterData.date,
                onValueChange = { millis ->
//                    val newValue =
//                        if (millis == null) emptyList<Long>()
//                        else {
//                            val from = LocalDateTime(millis.toLocalDate(), LocalTime(0, 0, 0))
//                            val to = LocalDateTime(millis.toLocalDate(), LocalTime(23, 59, 59))
//
//                            listOf(from.toEpochMillis(), to.toEpochMillis())
//                        }

                    cb.onFilter(
                        uiState.filterData.copy(
                            date = millis
                        )
                    )
                }
            )
        }
        Chip(
            text = uiState.user.classX ?: "-",
            severity = Severity.SECONDARY
        )
    }
}

@Composable
private fun AttendanceMobileContent(
    uiState: AttendanceUiState,
    cb: AttendanceCallback,
    pagingData: LazyPagingItems<AttendanceByClassEntity>,
    onNavigateTo: (String) -> Unit,
) {
    var showDetail by remember { mutableStateOf(false) }

    AttendancePaging(
        onClickAction = { item, action ->
            when (action) {
                AttendanceHomeAction.HISTORY_BY_STUDENT -> {
                    onNavigateTo(AttendanceRoute.AttendanceByStudentRoute.navigate(item.student.id))
                }

                AttendanceHomeAction.DETAIL -> {
                    cb.onGetDetail(item)
                    showDetail = true
                }
            }
        },
        pagingData = pagingData,
        cb = cb
    )

    AttendanceDetailDialog(
        showDialog = showDetail,
        onDismissRequest = { showDetail = it },
        isLoading = uiState.isLoadingDetail,
        detail = uiState.attendanceDetail
    )
}
