package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.apiservice.model.common.Role
import com.rollinup.rollinup.component.export.ExportAlertDialog
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.theme.localUser
import com.rollinup.rollinup.component.topbar.TopBar
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancebystudent.AttendanceByStudentFilterData
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.uistate.AttendanceByStudentUiState

@Composable
fun AttendanceByStudentTopBar(
    onNavigateUp: () -> Unit,
    uiState: AttendanceByStudentUiState,
    onUpdateFilter: (AttendanceByStudentFilterData) -> Unit,
    onExportFile: (String) -> Unit,
) {
    var showFilter by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    val isTeacher = localUser?.role == Role.TEACHER
    val menu = buildList {
        if (isTeacher) add(Menu.PRINT)
        add(Menu.FILTER)
    }

    TopBar(
        onClickMenu = { menu ->
            when (menu) {
                Menu.FILTER -> {
                    showFilter = true
                }

                Menu.PRINT -> {
                    showExportDialog = true
                }

                else -> {}
            }
        },
        title = "Student Attendance",
        menu = menu,
        onNavigateUp = onNavigateUp
    )

    AttendanceByStudentFilterSheet(
        showFilter = showFilter,
        onDismissRequest = { showFilter = it },
        uiState = uiState,
        onUpdateFilter = onUpdateFilter,
    )

    ExportAlertDialog(
        isShowDialog = showExportDialog,
        fileName = "${uiState.student.fullName}-Attendance",
        onDismissRequest = { showExportDialog = it },
        onConfirm = onExportFile
    )
}