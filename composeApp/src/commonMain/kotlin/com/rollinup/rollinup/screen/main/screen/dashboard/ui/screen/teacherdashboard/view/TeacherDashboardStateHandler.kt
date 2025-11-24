package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view

import androidx.compose.runtime.Composable
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState

@Composable
fun TeacherDashboardStateHandler(
    uiState: TeacherDashboardUiState,
    onRefresh: () -> Unit,
    onResetMessageState: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    HandleState(
        state = uiState.submitApprovalState,
        successMsg = "Success, permit approval successfully submitted",
        errorMsg = "Error, failed to submit permit approval please try again",
        onDispose = onResetMessageState,
        onSuccess = onRefresh,
        onShowSnackBar = onShowSnackBar
    )
    HandleState(
        state = uiState.submitEditAttendanceState,
        successMsg = "Success, attendance data successfully edited",
        errorMsg = "Error, failed to edit attendance data please try again",
        onDispose = onResetMessageState,
        onSuccess = onRefresh,
        onShowSnackBar = onShowSnackBar
    )
}