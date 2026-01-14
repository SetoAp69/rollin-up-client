package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.theme.localUser
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.viewmodel.StudentDashboardViewmodel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_check_in_error
import rollin_up.composeapp.generated.resources.msg_check_in_success

@Composable
fun StudentDashboardScreen(
    onShowSnackBar: OnShowSnackBar,
    onNavigateTo: (String) -> Unit,
    onRefreshSetting: () -> Unit,
) {
    val viewModel: StudentDashboardViewmodel = koinViewModel()
    val cb = viewModel.getCallback()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val loginData = localUser

    LaunchedEffect(Unit) {
        viewModel.init(loginData)
    }
    HandleState(
        state = uiState.checkInState,
        successMsg = stringResource(Res.string.msg_check_in_success),
        errorMsg = stringResource(Res.string.msg_check_in_error),
        onDispose = cb.onResetMessageState,
        onSuccess = cb.onRefresh ,
        onShowSnackBar = onShowSnackBar,
    )
    StudentDashboardContent(
        onShowSnackBar = onShowSnackBar,
        uiState = uiState,
        cb = cb,
        onNavigateTo = onNavigateTo,
        onRefreshSetting = onRefreshSetting
    )
}

