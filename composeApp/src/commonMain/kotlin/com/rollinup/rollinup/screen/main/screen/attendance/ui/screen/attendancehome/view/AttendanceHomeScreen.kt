package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.theme.localUser
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.viewmodel.AttendanceViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AttendanceHomeScreen(
    onNavigateUp: () -> Unit,
    onNavigateTo: (String) -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    val viewModel: AttendanceViewModel = koinViewModel()
    val cb = viewModel.getCallback()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()
    val localUser = localUser
    val isMobile = getPlatform().isMobile()

    HandleState(
        state = uiState.exportState,
        successMsg = "Success, data successfully exported.",
        errorMsg = "Error, failed to export data, please try again.",
        onDispose = cb.onResetMessageState,
        onShowSnackBar = onShowSnackBar,
    )
    LaunchedEffect(Unit) {
        viewModel.init(localUser, isMobile)
    }
    if (getPlatform().isMobile()) {
        AttendanceMobileContent(
            uiState = uiState,
            cb = cb,
            pagingData = pagingData,
            onNavigateTo = onNavigateTo,
            onNavigateUp = onNavigateUp
        )
    } else {
        AttendanceDesktopContent(
            uiState = uiState,
            cb = cb,
        )
    }
}