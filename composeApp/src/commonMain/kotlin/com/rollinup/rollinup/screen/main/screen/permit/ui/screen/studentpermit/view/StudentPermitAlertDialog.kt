package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.view

import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.permit.PermitByStudentEntity
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.utils.toAnnotatedString
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_cancel
import rollin_up.composeapp.generated.resources.label_cancel_permit
import rollin_up.composeapp.generated.resources.label_confirm

@Composable
fun CancelPermitAlertDialog(
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onConfirm: () -> Unit,
    item: PermitByStudentEntity,
) {
    val message =
        "${item.type} permit for **${
            generateDuration(
                item.startTime,
                item.endTime
            )
        }** will be cancelled"

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

private fun generateDuration(from: String, to: String): String {
    val dateTimeFrom = from.parseToLocalDateTime()
    val dateTimeTo = to.parseToLocalDateTime()

    return when {
        dateTimeFrom == dateTimeTo -> {
            with(dateTimeFrom) {
                "${date.day} ${month.name.take(3)}"
            }
        }

        dateTimeFrom.date == dateTimeTo.date -> {
            val date = "${dateTimeFrom.date.dayOfWeek.name.take(3)}, ${dateTimeFrom.date.day} ${
                dateTimeFrom.month.name.take(3)
            }"
            val timeFrom = "${dateTimeFrom.hour}:${dateTimeFrom.minute}"
            val timeTo = "${dateTimeTo.hour}:${dateTimeTo.minute}"
            "$date $timeFrom - $timeTo"
        }

        else -> {
            val from = "${dateTimeFrom.dayOfWeek.name.take(3)}, ${dateTimeFrom.date.day} ${
                dateTimeFrom.date.month.name.take(3)
            }"
            val to = "${dateTimeTo.dayOfWeek.name.take(3)}, ${dateTimeTo.date.day} ${
                dateTimeTo.date.month.name.take(3)
            }"
            "$from - $to"
        }

    }
}