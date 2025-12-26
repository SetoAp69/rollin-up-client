package com.rollinup.rollinup.screen.main.screen.usercenter.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.viewmodel.UserCenterViewmodel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserCenterScreen(
    onShowSnackBar: OnShowSnackBar,
) {
    val viewModel: UserCenterViewmodel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val cb = viewModel.getCallback()

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    UserCenterContent(
        uiState = uiState,
        cb = cb,
        onShowSnackBar = onShowSnackBar
    )
}