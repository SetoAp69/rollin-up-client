package com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitapproval.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.permit.PermitDetailEntity
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.model.OptionData
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.handlestate.HandleState
import com.rollinup.rollinup.component.imageview.ImageView
import com.rollinup.rollinup.component.loading.LoadingOverlay
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.radio.RadioSelectorRow
import com.rollinup.rollinup.component.record.RecordField
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextField
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.permit.model.permitapproval.PermitApprovalCallback
import com.rollinup.rollinup.screen.main.screen.permit.model.permitapproval.PermitApprovalFormData
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitapproval.uistate.PermitApprovalUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PermitApprovalFormContent(
    uiState: PermitApprovalUiState,
    cb: PermitApprovalCallback,
    onDismissRequest: (Boolean) -> Unit,
    onShowSnackBar: OnShowSnackBar,
    onSuccess: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    LoadingOverlay(uiState.isLoadingOverlay)
    HandleState(
        state = uiState.submitState,
        successMsg = "Success, permit approval successfully submitted",
        errorMsg = "Error, failed to submit permit approval please try again",
        onDispose = cb.onResetMessageState,
        onSuccess = {
            scope.launch {
                delay(1000)
                onSuccess()
                onDismissRequest(false)
            }
        },
        onShowSnackBar = onShowSnackBar
    )

    PermitApprovalFormContent(
        uiState = uiState,
        cb = cb
    )
}

@Composable
private fun PermitApprovalFormContent(
    uiState: PermitApprovalUiState,
    cb: PermitApprovalCallback,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Permit Approval",
            style = Style.popupTitle,
            color = theme.bodyText
        )
        if (uiState.isSingle) {
            if (uiState.isLoading) {
                DetailLoading()
            } else {
                DetailSection(uiState.detail)
            }
        }
        FormContent(
            onUpdateForm = cb.onUpdateFormData,
            formData = uiState.formData
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            text = "Submit"
        ) {
            if (!cb.onValidate(uiState.formData)) return@Button
            cb.onSubmit(uiState.formData)
        }
    }
}

@Composable
private fun FormContent(
    onUpdateForm: (PermitApprovalFormData) -> Unit,
    formData: PermitApprovalFormData,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RadioSelectorRow(
            title = "Approval",
            value = formData.isApproved,
            onValueChange = {
                onUpdateForm(
                    formData.copy(
                        isApproved = it
                    )
                )
            },
            options =
                listOf(
                    OptionData(
                        label = "Approve",
                        value = true
                    ),
                    OptionData(
                        label = "Decline",
                        value = false
                    )
                ),
        )
        TextField(
            title = "Approval note",
            value = formData.approvalNote,
            placeholder = "Enter approval note",
            maxChar = 120,
            onValueChange = {
                val errorMessage =
                    if (it.length > 120) "Approval note must be less than 120 characters" else null
                onUpdateForm(
                    formData.copy(
                        approvalNote = it,
                        approvalNoteError = errorMessage
                    )
                )
            },
            isError = formData.approvalNoteError != null,
            errorMsg = formData.approvalNoteError

        )
    }
}

@Composable
fun DetailSection(
    detail: PermitDetailEntity,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(itemGap4)
    ) {
        RecordField(
            title = "Name",
            content = detail.student.name
        )
        RecordField(
            title = "Id",
            content = detail.student.studentId ?: "-"
        )
        RecordField(
            title = "Class",
            content = detail.student.xClass ?: "-"
        )
        detail.reason?.let {
            RecordField(
                title = "Reason",
                content = it
            )
        }
        RecordField(
            title = "Duration",
            content = getDuration(detail)
        )
        RecordField(
            title = "Permit Attachment"
        ) {
            AttachmentButton(detail.attachment)
        }
        RecordField(
            title = "Note",
            content = detail.note ?: "-"
        )

        RecordField(
            title = "Created at",
            content = detail.createdAt
        )
    }
}

@Composable
fun DetailLoading() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column() {
            repeat(5) {
                ShimmerEffect(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .height(18.dp)
                )
                Spacer(itemGap8)
            }
        }
    }
}


@Composable
private fun AttachmentButton(
    url: String,
) {
    var showImage by remember { mutableStateOf(false) }
    val text = if (url.isBlank()) "-" else "View Attachment"

    Text(
        text = text,
        color = theme.textPrimary,
        style = Style.title,
        modifier = Modifier.clickable {
            if (url.isNotBlank()) {
                showImage = true
            }
        }
    )

    ImageView(
        showView = showImage,
        onDismissRequests = { showImage = it },
        url = url
    )
}

private fun getDuration(
    permit: PermitDetailEntity,
): String {
    val from = permit.startTime.parseToLocalDateTime()
    val to = permit.endTime.parseToLocalDateTime()

    return when (
        permit.type
    ) {
        PermitType.DISPENSATION -> {
            "${from.time} - ${to.time}"
        }

        PermitType.ABSENCE -> {
            val fromDate = from.date
            val toDate = to.date

            if (fromDate == toDate) {
                "${from.time}"
            } else {
                "${from.time} - ${to.time}"

            }
        }
    }
}