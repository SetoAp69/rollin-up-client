package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view.table

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.date.DateFormatter
import com.rollinup.rollinup.component.date.DateText
import com.rollinup.rollinup.component.date.DateTextFormat
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.dropdown.DropDownMenuItem
import com.rollinup.rollinup.component.model.getLabel
import com.rollinup.rollinup.component.table.Table
import com.rollinup.rollinup.component.table.TableColumn
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.uistate.AttendanceByStudentUiState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.label_attendance_history
import rollin_up.composeapp.generated.resources.label_check_in_time
import rollin_up.composeapp.generated.resources.label_date
import rollin_up.composeapp.generated.resources.label_detail
import rollin_up.composeapp.generated.resources.label_duration
import rollin_up.composeapp.generated.resources.label_reason
import rollin_up.composeapp.generated.resources.label_status

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
                text = stringResource(Res.string.label_attendance_history),
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
                    label = stringResource(Res.string.label_detail),
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
        TableColumn(Res.string.label_date, 0.5f) {
            val localDate = LocalDate.parse(it.date)
            Text(
                text = DateFormatter.formatDateShort(localDate, false),
                color = theme.bodyText,
                style = Style.body
            )

        },
        TableColumn(Res.string.label_status, 0.5f) {
            Chip(
                text = it.status.getLabel(),
                severity = it.status.severity
            )
        },
        TableColumn(Res.string.label_check_in_time) { data ->
            data.checkInTime?.let {
                DateText(
                    dateTime = it.parseToLocalDateTime(TimeZone.UTC),
                    format = DateTextFormat.TIME
                )
            }
        },
        TableColumn(Res.string.label_duration) { data ->
            val text = data.permit?.durationString ?: "-"
            Text(
                text = text,
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn(Res.string.label_reason, 0.5f) { data ->
            Text(
                text = data.permit?.reason ?: "-",
                color = theme.bodyText,
                style = Style.body
            )
        }
    )