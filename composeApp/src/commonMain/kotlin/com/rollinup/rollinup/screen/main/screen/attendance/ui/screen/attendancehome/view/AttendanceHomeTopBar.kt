package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.rollinup.component.date.DateRangePicker
import com.rollinup.rollinup.component.export.ExportAlertDialog
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.topbar.TopBar
import com.rollinup.rollinup.component.utils.getFileName
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceCallback
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.uistate.AttendanceUiState

@Composable
fun AttendanceHomeTopBar(
    uiState: AttendanceUiState,
    cb: AttendanceCallback,
    onNavigateUp: () -> Unit,
) {
    var showFilter by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    TopBar(
        onSearch = cb.onSearch,
        onClickMenu = { menu ->
            when (menu) {
                Menu.FILTER -> {
                    showFilter = true
                }

                Menu.PRINT -> {
                    showDatePicker = true
                }

                else -> {}
            }
        },
        menu = listOf(
            Menu.FILTER,
            Menu.PRINT
        ),
        onNavigateUp = onNavigateUp,
        title = "Attendance History",
    )
    AttendanceFilterSheet(
        showSheet = showFilter,
        onDismissRequest = { showFilter = it },
        uiState = uiState,
        onApply = cb.onFilter
    )

    DateRangePicker(
        isShowDatePicker = showDatePicker,
        onDismissRequest = { showDatePicker = it },
        value = uiState.exportDateRanges,
        onSelectDate = { value ->
            cb.onUpdateExportDateRange(value.sorted())
            showExportDialog = true
        },
        title = "Select date ranges",
        isDisablePastSelection = false,
        isAllSelectable = true,
    )

    ExportAlertDialog(
        isShowDialog = showExportDialog,
        fileName = getFileName(uiState.exportDateRanges, "attendance"),
        onDismissRequest = { showExportDialog = it },
        onConfirm = {
            cb.onExportFile(it)
            cb.onUpdateExportDateRange(emptyList())
        }
    )
}
