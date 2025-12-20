package com.rollinup.rollinup.component.filepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.ActionButton
import com.rollinup.rollinup.component.camera.CameraView
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.theme.Style
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_camera_line_24
import rollin_up.composeapp.generated.resources.ic_document_line_24
import rollin_up.composeapp.generated.resources.label_open_camera
import rollin_up.composeapp.generated.resources.label_open_file
import rollin_up.composeapp.generated.resources.msg_camera_error_capture
import rollin_up.composeapp.generated.resources.msg_camera_face_position

@Composable
fun FilePickerBottomSheet(
    isShowSheet: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onSelectFile: (MultiPlatformFile) -> Unit,
) {
    var showCamera by remember { mutableStateOf(false) }
    var showFilePicker by remember { mutableStateOf(false) }

    BottomSheet(
        isShowSheet = isShowSheet,
        onDismissRequest = onDismissRequest,
    ) {
        ActionButton(
            label = stringResource(Res.string.label_open_camera),
            icon = Res.drawable.ic_camera_line_24,
        ) {
            showCamera = true
        }
        ActionButton(
            label = stringResource(Res.string.label_open_file),
            icon = Res.drawable.ic_document_line_24,
        ) {
            showFilePicker = true
        }
    }

    CameraView(
        onDismissRequest = { showCamera = it },
        onCapture = {
            onSelectFile(it)
            showCamera = false
            onDismissRequest(false)
        },
        notification = {
            Chip(
                text = stringResource(Res.string.msg_camera_face_position),
                severity = Severity.SECONDARY,
                textStyle = Style.body
            )
        },
        isShowCamera = showCamera,
        errorMsg = stringResource(Res.string.msg_camera_error_capture),
    )

    FileHandler(
        onFileSelected = {
            onSelectFile(it)
            showFilePicker = false
            onDismissRequest(false)
        },
        value = null,
        allowedType = listOf(
            MimeType.DOCUMENT_PDF_TYPE,
            MimeType.IMAGE_TYPE
        ),
        isLaunchHandler = showFilePicker
    )

}