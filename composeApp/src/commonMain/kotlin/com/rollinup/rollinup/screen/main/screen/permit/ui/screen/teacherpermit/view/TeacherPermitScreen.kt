package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.theme.localUser
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.viewmodel.TeacherPermitViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TeacherPermitScreen(
    onNavigateUp: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    val viewModel: TeacherPermitViewModel = koinViewModel()
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val cb = viewModel.getCallback()
    val localUser = localUser

    LaunchedEffect(localUser) {
        viewModel.init(localUser)
    }
    HandleState(
        state = uiState.exportState,
        successMsg = "Success, data successfully exported",
        errorMsg = "Error, failed to export data, please try again",
        onDispose = cb.onResetMessageState,
        onShowSnackBar = onShowSnackBar,
    )
    if (getPlatform().isMobile()) {
        TeacherPermitMobileContent(
            uiState = uiState,
            onNavigateUp = onNavigateUp,
            pagingData = pagingData,
            cb = cb
        )
    } else {
        TeacherPermitDesktopContent(
            uiState = uiState,
            cb = cb
        )
    }
}