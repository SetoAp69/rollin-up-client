package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.topbar.TopBar
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceCallback
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.uistate.AttendanceUiState

@Composable
fun AttendanceHomeTopBar(
    uiState: AttendanceUiState,
    cb: AttendanceCallback,
    onNavigateUp: () -> Unit,
) {
    var showFilter by remember { mutableStateOf(false) }
    TopBar(
        onSearch = cb.onSearch,
        onClickMenu = { menu ->
            when (menu) {
                Menu.FILTER -> {
                    showFilter = true
                }

                Menu.PRINT -> {
                    /*TODO: ADD PRINT FUNCTIONALITY*/
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
}