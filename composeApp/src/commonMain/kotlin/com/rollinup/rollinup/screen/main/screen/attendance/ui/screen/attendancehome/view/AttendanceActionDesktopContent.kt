package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.rollinup.component.attendancedetail.AttendanceDetailDialog
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceCallback
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceHomeAction
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view.AttendanceByStudentDialog
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.uistate.AttendanceUiState
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view.table.AttendanceTable
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view.table.AttendanceTableFilter

@Composable
fun AttendanceDesktopContent(
    uiState: AttendanceUiState,
    cb: AttendanceCallback,
) {
    var showDetail by remember { mutableStateOf(false) }
    var showHistoryByStudent by remember { mutableStateOf(false) }
    var selectedId: String? by remember { mutableStateOf(null) }

    Scaffold {
        AttendanceDesktopContent(
            uiState = uiState,
            cb = cb,
            onClickAction = { data, action ->
                when (action) {
                    AttendanceHomeAction.HISTORY_BY_STUDENT -> {
                        showHistoryByStudent = true
                        selectedId = data.attendance?.id
                    }

                    AttendanceHomeAction.DETAIL -> {
                        cb.onGetDetail(data)
                        showDetail = true

                    }
                }
            }
        )
    }

    AttendanceDetailDialog(
        showDialog = showDetail,
        onDismissRequest = { showDetail = it },
        isLoading = uiState.isLoadingDetail,
        detail = uiState.attendanceDetail
    )

    selectedId?.let { id ->
        AttendanceByStudentDialog(
            showDialog = showHistoryByStudent,
            onDismissRequest = { showHistoryByStudent = it },
            id = id
        )
    }
}


@Composable
private fun AttendanceDesktopContent(
    uiState: AttendanceUiState,
    cb: AttendanceCallback,
    onClickAction: (AttendanceByClassEntity, AttendanceHomeAction) -> Unit,
) {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Attendance History",
            style = Style.headerBold,
            color = theme.textPrimary
        )
        AttendanceTableFilter(
            uiState = uiState,
            cb = cb
        )
        AttendanceTable(
            uiState = uiState,
            onClickAction = onClickAction
        )
    }
}