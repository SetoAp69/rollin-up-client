package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.view

import androidx.compose.runtime.Composable
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.screen.main.screen.permit.model.studentpermit.StudentPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.uistate.StudentPermitUiState
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_cancel_permit_error
import rollin_up.composeapp.generated.resources.msg_cancel_permit_success

@Composable
fun StudentPermitStateHandler(
    cb: StudentPermitCallback,
    uiState: StudentPermitUiState,
    onShowSnackBar: OnShowSnackBar,
) {

    HandleState(
        state = uiState.cancelState,
        successMsg = stringResource(Res.string.msg_cancel_permit_success),
        errorMsg = stringResource(Res.string.msg_cancel_permit_error),
        onDispose = cb.onResetMessageState,
        onShowSnackBar = onShowSnackBar,
    )
}