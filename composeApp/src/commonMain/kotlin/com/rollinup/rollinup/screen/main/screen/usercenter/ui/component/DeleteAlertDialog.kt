package com.rollinup.rollinup.screen.main.screen.usercenter.ui.component

import androidx.compose.runtime.Composable
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.toAnnotatedString
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_delete_line_24

@Composable
fun DeleteAlertDialog(
    items: List<String>,
    isShowDialog: Boolean,
    name: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismissRequest: (Boolean) -> Unit,
) {
    val message =
        if (items.size == 1) {
            "Are you sure you want to delete **${items.first()}**?"
        } else {
            "Are you sure you want to delete **${items.size} $name(s)** data?"
        }

    val title = "Delete $name(s)"

    AlertDialog(
        title = title,
        isShowDialog = isShowDialog,
        content = message.toAnnotatedString(),
        onClickConfirm = onConfirm,
        onClickCancel = onCancel,
        onDismissRequest = onDismissRequest,
        btnCancelText = "Cancel",
        btnConfirmText = "Confirm",
        severity = Severity.DANGER,
        icon = Res.drawable.ic_delete_line_24,
        iconTint = theme.danger
    )


}