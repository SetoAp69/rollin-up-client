package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rollinup.rollinup.component.dialog.AlertDialog
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.label_cancel
import rollin_up.composeapp.generated.resources.label_confirm_check_in
import rollin_up.composeapp.generated.resources.label_continue
import rollin_up.composeapp.generated.resources.msg_confirm_check_in

@Composable
fun CheckInConfirmDialog (
    onConfirm:()->Unit,
    onDismissRequest:(Boolean)->Unit,
    showDialog: Boolean
) {
    AlertDialog(
        icon = Res.drawable.ic_info_line_24,
        isShowDialog = showDialog,
        onDismissRequest = onDismissRequest,
        title = stringResource(Res.string.label_confirm_check_in ),
        content = stringResource(Res.string.msg_confirm_check_in),
        btnConfirmText = stringResource(Res.string.label_continue),
        btnCancelText = stringResource(Res.string.label_cancel),
        onClickConfirm = onConfirm,
        onClickCancel = {onDismissRequest(false)},
    )
}