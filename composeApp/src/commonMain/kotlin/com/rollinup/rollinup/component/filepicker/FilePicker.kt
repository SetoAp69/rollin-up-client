package com.rollinup.rollinup.component.filepicker

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.modifier.dashedBorder
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextError
import com.rollinup.rollinup.component.textfield.TextFieldDefaults
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.applyIf
import com.rollinup.rollinup.component.utils.getPlatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_delete_line_24
import rollin_up.composeapp.generated.resources.ic_edit_line_24
import rollin_up.composeapp.generated.resources.ic_upload_fill_24
import rollin_up.composeapp.generated.resources.label_upload

/**
 * Platform-specific file handler component.
 *
 * Expected to launch a file picker dialog or interface when `isLaunchHandler` is true,
 * and invoke `onFileSelected` with the selected file.
 *
 * @param onFileSelected Callback invoked with the selected file.
 * @param value The currently selected file (if any).
 * @param allowedType List of allowed MIME types for selection.
 * @param isLaunchHandler If true, triggers the file selection process.
 */

/**
 * Platform-specific handler for launching the system file picker.
 *
 * @param onFileSelected Callback invoked when a file is successfully picked.
 * @param value The currently selected file (if any).
 * @param allowedType List of allowed MIME types for file selection (e.g., "image/ *", "application/pdf").
 * @param isLaunchHandler When true, triggers the file picker launch.
 */
@Composable
expect fun FileHandler(
    onFileSelected: (MultiPlatformFile) -> Unit,
    value: MultiPlatformFile?,
    allowedType: List<String>,
    isLaunchHandler: Boolean,
)

/**
 * A form field component for file uploading with validation support.
 *
 * Wraps [FilePicker] with a title and error message display.
 *
 * @param title The label displayed above the picker.
 * @param isRequired Indicates if this field is mandatory (visual indicator).
 * @param value The currently selected file.
 * @param fileName Optional filename to display if `value` doesn't have a name or for initial state.
 * @param showCameraOption If true (and on mobile), allows taking a photo via [FilePickerBottomSheet].
 * @param onValueChange Callback invoked when the selected file changes (picked or deleted).
 * @param isError If true, displays the component in an error state.
 * @param errorMsg Error text displayed below the component if `isError` is true.
 */
@Composable
fun FilePicker(
    title: String,
    isRequired: Boolean,
    value: MultiPlatformFile?,
    fileName: String? = null,
    showCameraOption: Boolean = false,
    onValueChange: (MultiPlatformFile?) -> Unit,
    isError: Boolean = false,
    errorMsg: String? = null,
) {
    TextFieldTitle(
        title = title,
        isRequired = isRequired
    ) {
        Column {
            FilePicker(
                value = value,
                fileName = fileName,
                showCameraOption = showCameraOption,
                onValueChange = onValueChange
            )
            Spacer(itemGap4)
            TextError(
                text = errorMsg ?: "",
                isError = isError
            )
        }

    }
}

/**
 * The core file picker component.
 *
 * Renders a dashed border box when empty (upload prompt) or a solid border box with
 * filename and actions (edit/delete) when a file is selected.
 *
 * @param value The currently selected file.
 * @param fileName Optional filename string override.
 * @param showCameraOption If true, provides camera access on mobile devices.
 * @param onValueChange Callback invoked with the new file or null (deleted).
 * @param isError Affects the border and text color to indicate error state.
 */
@Composable
fun FilePicker(
    value: MultiPlatformFile?,
    fileName: String? = null,
    showCameraOption: Boolean = false,
    onValueChange: (MultiPlatformFile?) -> Unit,
    isError: Boolean = false,
) {
    var tempValue: MultiPlatformFile? by remember { mutableStateOf(null) }
    var showHandler by remember { mutableStateOf(false) }

    val lineColor: Color
    val contentColor: Color

    if (isError) {
        lineColor = theme.danger
        contentColor = theme.danger
    } else {
        lineColor = theme.textPrimary
        contentColor = theme.textPrimary
    }

    LaunchedEffect(value) {
        tempValue = value
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .applyIf(
                tempValue == null
            ) {
                dashedBorder(
                    width = 2.dp,
                    color = lineColor,
                    shape = RoundedCornerShape(50),
                    on = 8.dp,
                    off = 4.dp
                )
            }
            .applyIf(
                tempValue != null
            ) {
                border(
                    width = 2.dp,
                    brush = SolidColor(
                        value = lineColor
                    ),
                    shape = RoundedCornerShape(50)
                )
            }
            .clip(
                shape = RoundedCornerShape(50)
            )
            .height(TextFieldDefaults.height),
        contentAlignment = Alignment.Center
    ) {
        if (tempValue != null || fileName != null) {
            FilePickerPickedContent(
                fileName = tempValue?.name ?: fileName!!,
                onClickDelete = {
                    tempValue = null
                },
                onClickEdit = {
                    showHandler = true
                },
                contentColor = contentColor
            )
        } else {
            FilePickerNullContent(
                onClick = {
                    showHandler = true
                },
                contentColor = contentColor
            )
        }

        if (showCameraOption && getPlatform().isMobile()) {
            FilePickerBottomSheet(
                isShowSheet = showHandler,
                onDismissRequest = {
                    showHandler = it
                },
                onSelectFile = {
                    tempValue = it
                    onValueChange(tempValue)
                }
            )
        } else {
            FileHandler(
                onFileSelected = {
                    tempValue = it
                    showHandler = false
                    onValueChange(tempValue)
                },
                value = tempValue,
                allowedType = listOf(MimeType.DOCUMENT_TYPE),
                isLaunchHandler = showHandler
            )
        }
    }
}

/**
 * Renders the content when a file has been picked.
 *
 * Shows the filename, an edit button (to re-pick), and a delete button (to clear).
 */
@Composable
private fun FilePickerPickedContent(
    fileName: String,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    contentColor: Color,
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = fileName,
            modifier = Modifier.weight(1f),
            style = Style.body,
            color = contentColor
        )
        Spacer(itemGap8)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_edit_line_24),
                tint = contentColor,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onClickEdit()
                    }
            )
            Spacer(itemGap8)
            Icon(
                painter = painterResource(Res.drawable.ic_delete_line_24),
                tint = theme.danger,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onClickDelete()
                    }
            )
        }
    }

}

/**
 * Renders the placeholder content when no file is selected.
 *
 * Shows an upload icon and label.
 */
@Composable
private fun FilePickerNullContent(
    onClick: () -> Unit,
    contentColor: Color,
) {
    Row(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .fillMaxSize()
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_upload_fill_24),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier
                .size(16.dp)
        )
        Spacer(itemGap8)
        Text(
            text = stringResource(Res.string.label_upload),
            style = Style.body,
            color = contentColor
        )
    }
}