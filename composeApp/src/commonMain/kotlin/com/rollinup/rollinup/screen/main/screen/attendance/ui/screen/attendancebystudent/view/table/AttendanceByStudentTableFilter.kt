package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view.table

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.button.IconButton
import com.rollinup.rollinup.component.date.FilterDatePicker
import com.rollinup.rollinup.component.export.ExportAlertDialog
import com.rollinup.rollinup.component.filter.TableFilterRow
import com.rollinup.rollinup.component.selector.MultiDropDownSelector
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancebystudent.AttendanceByStudentFilterData
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.uistate.AttendanceByStudentUiState
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view.AttendanceByStudentSummary
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_print_line_24

@Composable
fun AttendanceByStudentTableFilter(
    uiState: AttendanceByStudentUiState,
    onUpdateFilter: (AttendanceByStudentFilterData) -> Unit,
    onExportFile: (String) -> Unit,
) {
    var showExportDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .width(400.dp)
                .padding(horizontal = 12.dp)
        ) {
            AttendanceByStudentSummary(
                isLoading = uiState.isLoadingSummary,
                summary = uiState.summary
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            icon = Res.drawable.ic_print_line_24
        ) {
            showExportDialog = true
        }
        AttendanceByStudentFilterRow(uiState, onUpdateFilter)
    }
    ExportAlertDialog(
        isShowDialog = showExportDialog,
        fileName = "${uiState.student.fullName}-Attendance",
        onDismissRequest = { showExportDialog = it },
        onConfirm = onExportFile
    )
}

@Composable
private fun AttendanceByStudentFilterRow(
    uiState: AttendanceByStudentUiState,
    onUpdateFilter: (AttendanceByStudentFilterData) -> Unit,
) {
    TableFilterRow(
        onReset = { onUpdateFilter(AttendanceByStudentFilterData()) },
        showReset = uiState.filterData != AttendanceByStudentFilterData(),
    ) {
        Box(modifier = Modifier.width(150.dp)) {
            FilterDatePicker(
                title = "Date",
                value = uiState.filterData.dateRange,
                enabled = true,
                onValueChange = { date ->
                    onUpdateFilter(
                        uiState.filterData.copy(
                            dateRange = date.sorted()
                        )
                    )
                },
            )
        }
        MultiDropDownSelector(
            title = "Status",
            options = uiState.statusOptions,
            value = uiState.filterData.status,
            onValueChange = { value ->
                onUpdateFilter(
                    uiState.filterData.copy(
                        status = value
                    )
                )
            }
        )
    }
}