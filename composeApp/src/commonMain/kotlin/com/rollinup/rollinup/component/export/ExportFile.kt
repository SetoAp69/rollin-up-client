package com.rollinup.rollinup.component.export

import androidx.compose.runtime.Composable
import com.rollinup.common.utils.Utils.now
import com.rollinup.rollinup.component.date.DateFormatter
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.utils.toAnnotatedString
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.koin.core.module.Module
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_gear_line_24

object ExportFileModule {
    operator fun invoke() = fileExporterModule()
}

expect fun fileExporterModule(): Module

@Composable
fun ExportAlertDialog(
    isShowDialog: Boolean,
    fileName: String,
    onDismissRequest: (Boolean) -> Unit,
    onConfirm: (String) -> Unit,
) {
    val exportedFileName = "$fileName-${LocalDate.now()}"

    AlertDialog(
        title = "Export File",
        icon = Res.drawable.ic_gear_line_24,
        content = "The $fileName data will be exported to your default download path as **$exportedFileName**".toAnnotatedString(),
        onClickCancel = { onDismissRequest(false) },
        onClickConfirm = { onConfirm(exportedFileName) },
        btnCancelText = "Cancel",
        btnConfirmText = "Export",
        isShowDialog = isShowDialog,
        onDismissRequest = onDismissRequest
    )
}