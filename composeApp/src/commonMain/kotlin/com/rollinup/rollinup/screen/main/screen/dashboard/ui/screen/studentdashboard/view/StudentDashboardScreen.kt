package com.rollinup.rollinup.screen.dashboard.ui.screen.studentdashboard.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.theme.localUser
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.view.StudentDashboardContent
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.viewmodel.StudentDashboardViewmodel
import org.koin.compose.viewmodel.koinViewModel

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

    StudentDashboardContent(
        onShowSnackBar = onShowSnackBar,
        uiState = uiState,
        cb = cb,
        onNavigateTo = onNavigateTo,
        onRefreshSetting = onRefreshSetting
    )
}

