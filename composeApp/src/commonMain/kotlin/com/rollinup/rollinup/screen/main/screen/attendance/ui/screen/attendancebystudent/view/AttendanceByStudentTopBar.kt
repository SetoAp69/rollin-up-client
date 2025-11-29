package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.topbar.TopBar
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.uistate.AttendanceByStudentUiState

@Composable
fun AttendanceByStudentTopBar(
    onNavigateUp: () -> Unit,
    uiState: AttendanceByStudentUiState,
    onSelectStatus: (List<AttendanceStatus>) -> Unit,
) {
    val menu = listOf(Menu.PRINT, Menu.FILTER)
    var showFilter by remember { mutableStateOf(false) }

    TopBar(
        onClickMenu = { menu ->
            when (menu) {
                Menu.FILTER -> {
                    showFilter = true
                }

                Menu.PRINT -> {/*TODO: ADD PRINT FUNCTIONALITY*/
                }

                else -> {}
            }
        },
        title ="Student Attendance",
        menu = menu,
        onNavigateUp = onNavigateUp
    )

    AttendanceByStudentFilterSheet(
        showFilter = showFilter,
        onDismissRequest = { showFilter = it },
        options = uiState.statusOptions,
        value = uiState.statusSelected,
        onValueChange = onSelectStatus
    )
}