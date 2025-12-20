package com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.view

import androidx.compose.runtime.Composable
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.theme.theme
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.label_close
import rollin_up.composeapp.generated.resources.label_first_time_login
import rollin_up.composeapp.generated.resources.label_next
import rollin_up.composeapp.generated.resources.label_unauthorized_device
import rollin_up.composeapp.generated.resources.msg_first_time_login
import rollin_up.composeapp.generated.resources.msg_unauthorized_device

@Composable
fun UnauthorizedDeviceDialog(
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onClickConfirm: () -> Unit,
) {
    AlertDialog(
        isShowDialog = showDialog,
        onDismissRequest = onDismissRequest,
        icon = Res.drawable.ic_info_line_24,
        iconTint = theme.danger,
        title = stringResource(Res.string.label_unauthorized_device),
        content = stringResource(Res.string.msg_unauthorized_device),
        onClickConfirm = onClickConfirm,
        isSingleButton = true,
        showCancelButton = true,
        btnCancelText = stringResource(Res.string.label_close),
        severity = Severity.DANGER,
        onClickCancel = { onDismissRequest(false) }
    )
}

@Composable
fun FirstTimeLoginDialog(
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onClickConfirm: () -> Unit,
) {
    AlertDialog(
        isShowDialog = showDialog,
        onDismissRequest = onDismissRequest,
        icon = Res.drawable.ic_info_line_24,
        iconTint = theme.primary,
        title = stringResource(Res.string.label_first_time_login),
        content = stringResource(Res.string.msg_first_time_login),
        onClickConfirm = {
            onDismissRequest(false)
            onClickConfirm()
        },
        isSingleButton = true,
        showCancelButton = false,
        btnCancelText = stringResource(Res.string.label_next),
        severity = Severity.PRIMARY,
        onClickCancel = {}
    )
}