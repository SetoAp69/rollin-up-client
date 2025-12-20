package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.theme.localUser
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.viewmodel.StudentCenterViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_export_error
import rollin_up.composeapp.generated.resources.msg_export_success

@Composable
fun StudentCenterScreen(
    onNavigateUp: () -> Unit,
    onNavigateTo: (String) -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    val viewModel: StudentCenterViewModel = koinViewModel()
    val cb = viewModel.getCallback()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()
    val localUser = localUser

    LaunchedEffect(localUser) {
        viewModel.init(localUser)
    }
    HandleState(
        state = uiState.exportState,
        successMsg = stringResource(Res.string.msg_export_success),
        errorMsg = stringResource(Res.string.msg_export_error),
        onDispose = cb.onResetMessageState,
        onShowSnackBar = onShowSnackBar,
    )
    if (viewModel.isMobile) {
        StudentCenterMobileContent(
            uiState = uiState,
            cb = cb,
            onNavigateTo = onNavigateTo,
            pagingData = pagingData,
            onNavigateUp = onNavigateUp
        )
    } else {
        StudentCenterDesktopContent(
            uiState = uiState,
            cb = cb
        )
    }
}