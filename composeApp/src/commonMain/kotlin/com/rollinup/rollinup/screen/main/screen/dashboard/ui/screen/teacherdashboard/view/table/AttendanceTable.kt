package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.table

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.rollinup.component.attendancedetail.AttendanceDetailDialog
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.date.DateText
import com.rollinup.rollinup.component.date.DateTextFormat
import com.rollinup.rollinup.component.table.Table
import com.rollinup.rollinup.component.table.TableColumn
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardAction
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.TeacherDashboardActionDropDown
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.TeacherDashboardApprovalSheet
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.TeacherDashboardEditAttendance
import kotlinx.datetime.TimeZone

@Composable
fun AttendanceTable(
    uiState: TeacherDashboardUiState,
    cb: TeacherDashboardCallback,
) {
    var showEdit by remember { mutableStateOf(false) }
    var showApproval by remember { mutableStateOf(false) }
    var showDetail by remember { mutableStateOf(false) }

    Table(
        items = uiState.attendanceList,
        isLoading = uiState.isLoadingList,
        columns = getTableColumn(),
        headerContent = {
            Text(
                text = "Today's Attendance",
                color = theme.textPrimary,
                style = Style.popupTitle
            )
        },
        itemSelected = uiState.itemSelected,
        onSelectItem = {
            cb.onUpdateSelection(it)
        },
        onToggleSelectAll = {
            cb.onSelectAll()
        },
        onRefresh = cb.onRefresh,
        dropDownMenu = { state ->
            TeacherDashboardActionDropDown(
                showDropDown = state.expanded,
                onDismissRequest = state.onDismissRequest,
                itemList = state.selectedItem,
            ) { action ->
                when (action) {
                    TeacherDashboardAction.EDIT_DATA -> {
                        showEdit = true
                    }

                    TeacherDashboardAction.APPROVAL -> {
                        showApproval = true
                    }

                    TeacherDashboardAction.DETAIL -> {
                        showDetail = true
                    }
                }
                if (state.selectedItem.size == 1) {
                    cb.onGetDetail(state.selectedItem.first())
                }
                state.onDismissRequest(false)
            }
        },
    )

    AttendanceDetailDialog(
        showDialog = showDetail,
        onDismissRequest = {
            showDetail = it
        },
        isLoading = uiState.isLoadingDetail,
        detail = uiState.attendanceDetail
    )

    TeacherDashboardApprovalSheet(
        showSheet = showApproval,
        onDismissRequest = { showApproval = it },
        uiState = uiState,
        cb = cb
    )

    TeacherDashboardEditAttendance(
        isShowForm = showEdit,
        onDismissRequest = {
            showEdit = it
            cb.onResetEditForm()
        },
        uiState = uiState,
        cb = cb
    )
}

private fun getTableColumn(): List<TableColumn<AttendanceByClassEntity>> {
    return listOf(
        TableColumn(
            title = "Id",
            weight = 0.5f,
            content = {
                Text(
                    text = it.student.studentId,
                    style = Style.body,
                    color = theme.bodyText
                )
            }
        ),
        TableColumn("Name") {
            Text(
                text = it.student.name,
                style = Style.body,
                color = theme.bodyText
            )
        },
        TableColumn("Status", 1f) {
            val status = it.attendance?.status ?: AttendanceStatus.NO_DATA
            Chip(
                text = status.label,
                severity = status.severity
            )
        },
        TableColumn("Check in time", 0.7f) {
            it.attendance?.checkedInAt?.let { dt ->
                DateText(
                    dateTime = dt.parseToLocalDateTime(TimeZone.UTC),
                    format = DateTextFormat.DATE_TIME,
                )
            } ?: Text(
                text = "-",
                style = Style.body,
                color = theme.bodyText
            )
        },
        TableColumn("Duration") {
            Text(
                text = it.permit?.durationString ?: "-",
                style = Style.body,
                color = theme.bodyText
            )
        },
        TableColumn("Reason") {
            Text(
                text = it.permit?.reason ?: "-",
                style = Style.body,
                color = theme.bodyText
            )
        }
    )
}