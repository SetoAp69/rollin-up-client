package com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.view

import androidx.compose.runtime.Composable
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.uistate.UpdatePasswordUiState

@Composable
fun UpdatePasswordStateHandler(
    uiState: UpdatePasswordUiState,
    onSuccess: () -> Unit,
    onDispose: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    HandleState(
        state = uiState.submitOtpState,
        successMsg = "Success, Otp successfully sent",
        errorMsg = "Error, failed to validate Otp, please try again or request a new otp.",
        onDispose = onDispose,
        onShowSnackBar = onShowSnackBar,
    )

    HandleState(
        state = uiState.requestOtpState,
        successMsg = "Success, new otp successfully sent to your email.",
        errorMsg = "Error, failed to send new otp, pease try again.",
        onDispose = onDispose,
        onShowSnackBar = onShowSnackBar
    )

    HandleState(
        state = uiState.updatePasswordState,
        successMsg = "Success, your account password successfully updated.",
        errorMsg = "Error, failed to update password, please try again.",
        onDispose = onDispose,
        onShowSnackBar = onShowSnackBar,
        onSuccess = onSuccess
    )
}