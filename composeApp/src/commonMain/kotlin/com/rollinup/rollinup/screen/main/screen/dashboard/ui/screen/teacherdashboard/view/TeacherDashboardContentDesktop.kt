package com.rollinup.rollinup.screen.dashboard.ui.screen.teacherdashboard.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.table.AttendanceTable
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.table.AttendanceTableHeader

@Composable
fun TeacherDashboardContentDesktop(
    onNavigateTo: (String) -> Unit,
    uiState: TeacherDashboardUiState,
    cb: TeacherDashboardCallback,
) {
    Scaffold {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            AttendanceTableHeader(
                uiState = uiState,
                cb = cb
            )
            Spacer(12.dp)
            AttendanceTable(
                uiState = uiState,
                cb = cb
            )
        }
    }
}