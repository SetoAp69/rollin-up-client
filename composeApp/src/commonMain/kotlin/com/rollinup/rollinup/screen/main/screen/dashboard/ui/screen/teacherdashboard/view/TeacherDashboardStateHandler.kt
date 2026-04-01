package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view

import androidx.compose.runtime.Composable
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_approval_error
import rollin_up.composeapp.generated.resources.msg_approval_success
import rollin_up.composeapp.generated.resources.msg_export_error
import rollin_up.composeapp.generated.resources.msg_export_success

@Composable
fun TeacherDashboardStateHandler(
    uiState: TeacherDashboardUiState,
    onRefresh: () -> Unit,
    onResetMessageState: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    HandleState(
        state = uiState.submitApprovalState,
        successMsg = stringResource(Res.string.msg_approval_success),
        errorMsg = stringResource(Res.string.msg_approval_error),
        onDispose = onResetMessageState,
        onSuccess = onRefresh,
        onShowSnackBar = onShowSnackBar
    )
    HandleState(
        state = uiState.exportState,
        successMsg = stringResource(Res.string.msg_export_success),
        errorMsg = stringResource(Res.string.msg_export_error),
        onDispose = onResetMessageState,
        onShowSnackBar = onShowSnackBar,
    )
}