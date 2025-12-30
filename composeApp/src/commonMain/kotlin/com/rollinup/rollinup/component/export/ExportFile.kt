package com.rollinup.rollinup.component.export

import androidx.compose.runtime.Composable
import com.rollinup.common.utils.Utils.now
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.utils.toAnnotatedString
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import org.koin.core.module.Module
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_file_export_line_24
import rollin_up.composeapp.generated.resources.label_cancel
import rollin_up.composeapp.generated.resources.label_confirm
import rollin_up.composeapp.generated.resources.msg_export_alert_dialog
import rollin_up.composeapp.generated.resources.welcome_message

/**
 * Koin module provider for the file export functionality.
 *
 * This object wraps the platform-specific [fileExporterModule].
 */
object ExportFileModule {
    operator fun invoke() = fileExporterModule()
}

/**
 * Expect function to provide the platform-specific Koin module for file exporting.
 *
 * Implementations should provide the concrete [FileWriter] instance.
 */
expect fun fileExporterModule(): Module

/**
 * A dialog that asks the user to confirm a file export operation.
 *
 * It automatically appends the current date to the proposed filename.
 *
 * @param isShowDialog Controls the visibility of the dialog.
 * @param fileName The base name of the file to be exported (without date suffix).
 * @param onDismissRequest Callback triggered when the dialog is dismissed or cancelled.
 * @param onConfirm Callback triggered when the export is confirmed. Receives the final formatted filename.
 */
@Composable
fun ExportAlertDialog(
    isShowDialog: Boolean,
    fileName: String,
    onDismissRequest: (Boolean) -> Unit,
    onConfirm: (String) -> Unit,
) {
    val exportedFileName = "$fileName-${LocalDate.now()}"
    val string = stringResource(Res.string.welcome_message, exportedFileName)
    AlertDialog(
        title = "Export File",
        icon = Res.drawable.ic_file_export_line_24,
        content = stringResource(Res.string.msg_export_alert_dialog).toAnnotatedString(),
        onClickCancel = { onDismissRequest(false) },
        onClickConfirm = { onConfirm(exportedFileName) },
        btnCancelText = stringResource(Res.string.label_cancel),
        btnConfirmText = stringResource(Res.string.label_confirm),
        isShowDialog = isShowDialog,
        onDismissRequest = onDismissRequest
    )
}