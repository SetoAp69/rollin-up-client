package com.rollinup.rollinup.component.filepicker

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.rollinup.component.modifier.dashedBorder
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextFieldDefaults
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.applyIf
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_delete_line_24
import rollin_up.composeapp.generated.resources.ic_edit_line_24
import rollin_up.composeapp.generated.resources.ic_upload_fill_24

@Composable
expect fun FileHandler(
    onFileSelected: (MultiPlatformFile) -> Unit,
    value: MultiPlatformFile?,
    allowedType: List<String>,
    isLaunchHandler: Boolean,
)

@Composable
fun FilePicker(modifier: Modifier = Modifier) {
    var value: MultiPlatformFile? by remember { mutableStateOf(null) }
    var showHandler by remember { mutableStateOf(false) }

    FileHandler(
        onFileSelected = {
            value = it
            showHandler = false
        },
        value = value,
        allowedType = listOf(MimeType.DOCUMENT_TYPE),
        isLaunchHandler = showHandler
    )



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .applyIf(
                value == null
            ) {
                dashedBorder(
                    width = 2.dp,
                    color = theme.primary,
                    shape = RoundedCornerShape(50),
                    on = 8.dp,
                    off = 4.dp
                )
            }
            .applyIf(
                value != null
            ) {
                border(
                    width = 2.dp,
                    brush = SolidColor(
                        value = theme.primary
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
        if (value != null) {
            FilePickerPickedContent(
                fileName = value?.name ?: "",
                onClickDelete = {
                    value = null
                },
                onClickEdit = {
                    showHandler = true
                }
            )
        } else {
            FilePickerNullContent(
                onClick = {
                    showHandler = true
                }
            )
        }
    }
}

@Composable
private fun FilePickerPickedContent(
    fileName: String,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
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
            color = theme.primary
        )
        Spacer(itemGap8)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_edit_line_24),
                tint = theme.primary,
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

@Composable
private fun FilePickerNullContent(
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_upload_fill_24),
            contentDescription = null,
            tint = theme.primary,
            modifier = Modifier
                .size(16.dp)
        )
        Spacer(itemGap8)
        Text(
            text = "Upload File",
            style = Style.body,
            color = theme.primary
        )
    }
}

