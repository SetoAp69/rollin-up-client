package com.rollinup.rollinup.component.permitform.view

import androidx.compose.runtime.Composable
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.utils.toAnnotatedString
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_cancel
import rollin_up.composeapp.generated.resources.label_cancel_permit
import rollin_up.composeapp.generated.resources.label_confirm
import rollin_up.composeapp.generated.resources.msg_create_permit_request_alert
import rollin_up.composeapp.generated.resources.msg_edit_permit_request_alert


@Composable
fun EditPermitAlertDialog(
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onConfirm: () -> Unit,
) {
    val message = stringResource(Res.string.msg_edit_permit_request_alert)

    AlertDialog(
        isShowDialog = showDialog,
        onDismissRequest = onDismissRequest,
        btnCancelText = stringResource(Res.string.label_cancel),
        btnConfirmText = stringResource(Res.string.label_confirm),
        title = stringResource(Res.string.label_cancel_permit),
        onClickCancel = { onDismissRequest(false) },
        onClickConfirm = onConfirm,
        content = message.toAnnotatedString(),
    )
}

@Composable
fun CreatePermitAlertDialog(
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onConfirm: () -> Unit,
) {
    val message = stringResource(Res.string.msg_create_permit_request_alert)
    AlertDialog(
        isShowDialog = showDialog,
        onDismissRequest = onDismissRequest,
        btnCancelText = stringResource(Res.string.label_cancel),
        btnConfirmText = stringResource(Res.string.label_confirm),
        title = stringResource(Res.string.label_confirm),
        onClickCancel = { onDismissRequest(false) },
        onClickConfirm = onConfirm,
        content = message.toAnnotatedString(),
    )
}