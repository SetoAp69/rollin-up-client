package com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view

import androidx.compose.runtime.Composable
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.uistate.ResetPasswordUiState

@Composable
fun ResetPasswordStateHandler(
    uiState: ResetPasswordUiState,
    onResetMessageState: () -> Unit,
    onNavigateUp: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    HandleState(
        state = uiState.submitEmailState,
        successMsg = "Success, email successfully sent ",
        errorMsg = "Error, failed to send email",
        onDispose = onResetMessageState,
        onShowSnackBar = onShowSnackBar,
    )
    HandleState(
        state = uiState.submitOtpState,
        successMsg = "Success, otp successfully sent ",
        errorMsg = "Error, failed to send otp",
        onDispose = onResetMessageState,
        onShowSnackBar = onShowSnackBar,
    )
    HandleState(
        state = uiState.submitPasswordState,
        successMsg = "Success, password successfully updated ",
        errorMsg = "Error, failed to update password",
        onDispose = onResetMessageState,
        onShowSnackBar = onShowSnackBar,
        onSuccess = onNavigateUp
    )
}