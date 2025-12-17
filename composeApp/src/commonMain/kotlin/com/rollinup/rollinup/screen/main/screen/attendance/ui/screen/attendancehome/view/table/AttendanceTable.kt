package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view.table

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.common.model.Severity
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.date.DateText
import com.rollinup.rollinup.component.table.Table
import com.rollinup.rollinup.component.table.TableColumn
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceHomeAction
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.uistate.AttendanceUiState
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view.AttendanceHomeActionDropdown
import kotlinx.datetime.TimeZone

@Composable
fun AttendanceTable(
    uiState: AttendanceUiState,
    onRefresh: () -> Unit,
    onClickAction: (AttendanceByClassEntity, AttendanceHomeAction) -> Unit,
) {
    Table(
        items = uiState.attendanceList,
        columns = getColumn(),
        isLoading = uiState.isLoading,
        showSelection = false,
        showActionMenu = true,
        onRefresh = onRefresh,
        dropDownMenu = { state ->
            AttendanceHomeActionDropdown(
                showDropdown = state.expanded,
                onDismissRequest = state.onDismissRequest,
                onClickAction = {
                    onClickAction(state.selectedItem.first(), it)
                }
            )
        }
    )
}


private fun getColumn(): List<TableColumn<AttendanceByClassEntity>> =
    listOf(
        TableColumn("Student ID", weight = 0.5f) {
            Text(
                text = it.student.studentId,
                style = Style.body,
                color = theme.bodyText
            )
        },
        TableColumn("Fullname") {
            Text(
                text = it.student.name,
                style = Style.body,
                color = theme.bodyText
            )
        },
        TableColumn("Status") {
            val status = it.attendance?.status ?: AttendanceStatus.NO_DATA
            Chip(
                text = status.label,
                severity = status.severity
            )
        },
        TableColumn("Check in Time") {
            it.attendance?.checkedInAt?.let { time ->
                DateText(time.parseToLocalDateTime(TimeZone.UTC))
            } ?: Text(
                text = "-",
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn("Duration") {
            Text(
                text = it.permit?.durationString ?: "-",
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn("Reason") {
            Chip(
                text = it.permit?.reason ?: "-",
                severity = Severity.SECONDARY
            )
        }

    )