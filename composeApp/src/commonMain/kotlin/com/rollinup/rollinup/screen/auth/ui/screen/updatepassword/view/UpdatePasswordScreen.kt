package com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.apiservice.model.common.Role
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.theme.localUser
import com.rollinup.rollinup.component.utils.getDeviceId
import com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.viewmodel.UpdatePasswordViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UpdatePasswordScreen(
    onSuccess: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    val viewModel: UpdatePasswordViewModel = koinViewModel()
    val cb = viewModel.getCallback()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val loginData = localUser
    val deviceId = if (loginData?.role == Role.STUDENT) getDeviceId() else loginData?.id

    DisposableEffect(Unit) {
        loginData?.let {
            viewModel.init(loginData)
            cb.onUpdateFormData(
                uiState.formData.copy(
                    deviceId = deviceId,
                    userId = it.id
                )
            )
        }
        onDispose { viewModel.reset() }
    }

    UpdatePasswordStateHandler(
        uiState = uiState,
        onSuccess = onSuccess,
        onDispose = cb.onResetMessageState,
        onShowSnackBar = onShowSnackBar
    )

    UpdatePasswordContent(
        cb = cb,
        uiState = uiState,
        onShowSnackBar = onShowSnackBar
    )
}