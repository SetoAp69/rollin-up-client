package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view.table

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.date.DateText
import com.rollinup.rollinup.component.date.DateTextFormat
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.dropdown.DropDownMenuItem
import com.rollinup.rollinup.component.table.Table
import com.rollinup.rollinup.component.table.TableColumn
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.uistate.AttendanceByStudentUiState
import kotlinx.datetime.TimeZone
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24

@Composable
fun AttendanceByStudentTable(
    uiState: AttendanceByStudentUiState,
    onRefresh: () -> Unit,
    onClickDetail: (AttendanceByStudentEntity) -> Unit,
) {
    Table(
        items = uiState.attendanceList,
        columns = getColumn(),
        showSelection = false,
        isLoading = uiState.isLoading,
        headerContent = {
            Text(
                text = "Attendance History",
                color = theme.textPrimary,
                style = Style.popupTitle
            )
        },
        onRefresh = onRefresh,
        dropDownMenu = { state ->
            DropDownMenu(
                isShowDropDown = state.expanded,
                onDismissRequest = state.onDismissRequest,
            ) {
                DropDownMenuItem(
                    label = "Detail",
                    icon = Res.drawable.ic_info_line_24
                ) {
                    onClickDetail(state.selectedItem.first())
                    state.onDismissRequest(false)
                }
            }
        },
    )
}

private fun getColumn(): List<TableColumn<AttendanceByStudentEntity>> =
    listOf(
        TableColumn("Date") {
            DateText(it.date)
        },
        TableColumn("Status",0.5f) {
            Chip(
                text = it.status.label,
                severity = it.status.severity
            )
        },
        TableColumn("Check in Time") { data ->
            data.checkInTime?.let {
                DateText(
                    dateTime = it.parseToLocalDateTime(TimeZone.UTC),
                    format = DateTextFormat.TIME
                )
            }
        },
        TableColumn("Duration", 0.5f) { data ->
            val text = data.permit?.durationString ?: "-"
            Text(
                text = text,
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn("Reason") { data ->
            Text(
                text = data.permit?.reason ?: "-",
                color = theme.bodyText,
                style = Style.body
            )
        }
    )