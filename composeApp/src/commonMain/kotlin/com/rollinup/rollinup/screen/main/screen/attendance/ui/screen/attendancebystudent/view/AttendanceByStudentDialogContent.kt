package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.rollinup.component.attendancedetail.AttendanceDetailDialog
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.loading.LoadingOverlay
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancebystudent.AttendanceByStudentCallback
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.uistate.AttendanceByStudentUiState
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view.table.AttendanceByStudentTable
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view.table.AttendanceByStudentTableFilter

@Composable
fun AttendanceByStudentDialogContent(
    uiState: AttendanceByStudentUiState,
    cb: AttendanceByStudentCallback,
    onShowSnackBar: OnShowSnackBar,
) {
    HandleState(
        state = uiState.exportState,
        successMsg = "Success, data successfully exported",
        errorMsg = "Error, failed to export data, please try again",
        onDispose = cb.onResetMessageState,
        onShowSnackBar = onShowSnackBar,
    )
    LoadingOverlay(uiState.isLoadingOverlay)
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.weight(1f)) {
            ProfileSection(
                user = uiState.student,
                isLoading = uiState.isLoadingProfile
            )
        }
        Spacer(24.dp)
        Box(modifier = Modifier.weight(3.5f)) {
            TableSection(
                uiState = uiState,
                cb = cb
            )
        }
    }

}

@Composable
private fun TableSection(
    uiState: AttendanceByStudentUiState,
    cb: AttendanceByStudentCallback,
) {
    var showDetail by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AttendanceByStudentTableFilter(
            uiState = uiState,
            onUpdateFilter = cb.onUpdateFilter,
            onExportFile = cb.onExportFile
        )
        AttendanceByStudentTable(
            uiState = uiState,
            onClickDetail = { item ->
                showDetail = true
                cb.onGetDetail(item.id)
            },
            onRefresh = cb.onRefresh
        )
    }

    AttendanceDetailDialog(
        showDialog = showDetail,
        onDismissRequest = { showDetail = it },
        isLoading = uiState.isLoadingDetail,
        detail = uiState.attendanceDetail
    )
}

@Composable
private fun ProfileSection(
    user: UserDetailEntity,
    isLoading: Boolean,
) {
    AttendanceByStudentProfileDesktop(
        user = user,
        isLoading = isLoading
    )
}