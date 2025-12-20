package com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.view

import androidx.compose.runtime.Composable
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.uistate.UpdatePasswordUiState
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_otp_send_error
import rollin_up.composeapp.generated.resources.msg_otp_send_success
import rollin_up.composeapp.generated.resources.msg_password_update_error
import rollin_up.composeapp.generated.resources.msg_password_update_success

@Composable
fun UpdatePasswordStateHandler(
    uiState: UpdatePasswordUiState,
    onSuccess: () -> Unit,
    onDispose: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    HandleState(
        state = uiState.submitOtpState,
        successMsg = stringResource(Res.string.msg_otp_send_success),
        errorMsg = stringResource(Res.string.msg_otp_send_error),
        onDispose = onDispose,
        onShowSnackBar = onShowSnackBar,
    )

    HandleState(
        state = uiState.requestOtpState,
        successMsg = stringResource(Res.string.msg_otp_send_success),
        errorMsg = stringResource(Res.string.msg_otp_send_error),
        onDispose = onDispose,
        onShowSnackBar = onShowSnackBar
    )

    HandleState(
        state = uiState.updatePasswordState,
        successMsg = stringResource(Res.string.msg_password_update_success),
        errorMsg = stringResource(Res.string.msg_password_update_error),
        onDispose = onDispose,
        onShowSnackBar = onShowSnackBar,
        onSuccess = onSuccess
    )
}