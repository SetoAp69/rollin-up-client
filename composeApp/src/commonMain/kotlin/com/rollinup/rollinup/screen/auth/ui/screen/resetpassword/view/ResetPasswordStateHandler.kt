package com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view

import androidx.compose.runtime.Composable
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.uistate.ResetPasswordUiState
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_email_send_error
import rollin_up.composeapp.generated.resources.msg_email_send_success
import rollin_up.composeapp.generated.resources.msg_otp_send_error
import rollin_up.composeapp.generated.resources.msg_otp_send_success
import rollin_up.composeapp.generated.resources.msg_password_update_error
import rollin_up.composeapp.generated.resources.msg_password_update_success

@Composable
fun ResetPasswordStateHandler(
    uiState: ResetPasswordUiState,
    onResetMessageState: () -> Unit,
    onNavigateUp: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    HandleState(
        state = uiState.submitEmailState,
        successMsg = stringResource(Res.string.msg_email_send_success),
        errorMsg = stringResource(Res.string.msg_email_send_error),
        onDispose = onResetMessageState,
        onShowSnackBar = onShowSnackBar,
    )
    HandleState(
        state = uiState.submitOtpState,
        successMsg = stringResource(Res.string.msg_otp_send_success),
        errorMsg = stringResource(Res.string.msg_otp_send_error),
        onDispose = onResetMessageState,
        onShowSnackBar = onShowSnackBar,
    )
    HandleState(
        state = uiState.submitPasswordState,
        successMsg = stringResource(Res.string.msg_password_update_success),
        errorMsg = stringResource(Res.string.msg_password_update_error),
        onDispose = onResetMessageState,
        onShowSnackBar = onShowSnackBar,
        onSuccess = onNavigateUp
    )
}