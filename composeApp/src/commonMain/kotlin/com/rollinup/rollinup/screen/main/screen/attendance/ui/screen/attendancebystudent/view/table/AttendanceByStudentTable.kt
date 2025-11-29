package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view.table

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.utils.Utils.toLocalDateTime
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
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24

@Composable
fun AttendanceByStudentTable(
    uiState: AttendanceByStudentUiState,
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
        TableColumn("Status") {
            Chip(
                text = it.status.label,
                severity = it.status.severity
            )
        },
        TableColumn("Check in Time") { data ->
            data.checkInTime?.let {
                DateText(
                    dateString = it,
                    format = DateTextFormat.TIME
                )
            }
        },
        TableColumn("Duration", 0.5f) { data ->
            val text = data.permit?.let { getDuration(it) } ?: "-"
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


private fun getDuration(
    permit: AttendanceByStudentEntity.Permit,
): String {
    val from = permit.start.toLocalDateTime()
    val to = permit.end.toLocalDateTime()

    return when (
        permit.type
    ) {
        PermitType.DISPENSATION -> {
            "${from.time} - ${to.time}"
        }

        PermitType.ABSENT -> {
            val fromDate = from.date
            val toDate = to.date

            if (fromDate == toDate) {
                "${from.time}"
            } else {
                "${from.time} - ${to.time}"

            }
        }
    }
}