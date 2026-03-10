package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.theme.localUser
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.viewmodel.StudentPermitViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentPermitScreen(
    onNavigateUp: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    val viewModel: StudentPermitViewModel = koinViewModel()
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val cb = viewModel.getCallback()
    val localUser = localUser

    LaunchedEffect(Unit) {
        viewModel.init(localUser)
    }
    StudentPermitStateHandler(
        uiState = uiState,
        onShowSnackBar = onShowSnackBar,
        cb = cb,
    )
    StudentPermitContent(
        uiState = uiState,
        pagingData = pagingData,
        cb = cb,
        onNavigateUp = onNavigateUp
    )
}