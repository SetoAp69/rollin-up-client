package com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.model.Platform
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view.desktop.ResetPasswordDesktopContent
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view.mobile.ResetPasswordMobileContent
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.viewmodel.ResetPasswordViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetPasswordScreen(
    onNavigateUp: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    val viewModel: ResetPasswordViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val cb = viewModel.getCallback()

    val platform = getPlatform()
    ResetPasswordStateHandler(
        uiState = uiState,
        onResetMessageState = cb.onResetMessageState,
        onShowSnackBar = onShowSnackBar,
        onNavigateUp = onNavigateUp
    )
    when (platform) {
        Platform.IOS, Platform.ANDROID -> {
            ResetPasswordMobileContent(
                onNavigateUp = onNavigateUp,
                uiState = uiState,
                cb = cb,
            )
        }

        else -> {
            ResetPasswordDesktopContent(
                onNavigateUp = onNavigateUp,
                uiState = uiState,
                cb = cb,
            )
        }
    }
}