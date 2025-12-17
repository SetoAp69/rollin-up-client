package com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.view

import androidx.compose.runtime.Composable
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.theme.theme
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24

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
        title = "Unauthorized Device",
        content = "Unauthorized Device detected, please login using your first device that is connected to this account",
        onClickConfirm = onClickConfirm,
        isSingleButton = true,
        showCancelButton = true,
        btnCancelText = "Close",
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
        title = "First Time Login Detected",
        content = "First time login detected, please update your password and try to login again using the new password",
        onClickConfirm = {
            onDismissRequest(false)
            onClickConfirm()
        },
        isSingleButton = true,
        showCancelButton = false,
        btnCancelText = "Next",
        severity = Severity.PRIMARY,
        onClickCancel = {}
    )
}