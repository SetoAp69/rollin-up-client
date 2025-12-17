package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.model.OptionData
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.dialog.Dialog
import com.rollinup.rollinup.component.imageview.ImageView
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.radio.RadioSelectorRow
import com.rollinup.rollinup.component.record.RecordField
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.textfield.TextField
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardApprovalFormData
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState

@Composable
fun TeacherDashboardApprovalSheet(
    showSheet: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    uiState: TeacherDashboardUiState,
    cb: TeacherDashboardCallback,
) {
    val platform = getPlatform()
    val detail =
        if (uiState.itemSelected.isNotEmpty()) null else uiState.attendanceDetail

    LaunchedEffect(uiState.submitApprovalState) {
        if (uiState.submitApprovalState == true) {
            onDismissRequest(false)
            cb.onResetSelection()
        }
    }

    if (platform.isMobile()) {
        BottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = Modifier.padding(screenPadding),
            isShowSheet = showSheet,
        ) {
            TeacherDashboardApprovalContent(
                detail = detail,
                onUpdateForm = cb.onUpdateApprovalForm,
                formData = uiState.approvalFormData,
                onSubmit = {
                    cb.onSubmitApproval(it)
                },
                isLoading = uiState.isLoadingDetail
            )
        }
    } else {
        Dialog(
            onDismissRequest = onDismissRequest,
            modifier = Modifier
                .padding(screenPadding)
                .width(400.dp),
            showDialog = showSheet,
        ) {
            TeacherDashboardApprovalContent(
                detail = detail,
                onUpdateForm = cb.onUpdateApprovalForm,
                formData = uiState.approvalFormData,
                onSubmit = {
                    cb.onSubmitApproval(it)
                    onDismissRequest(false)
                },
                isLoading = uiState.isLoadingDetail
            )
        }
    }
}


@Composable
private fun TeacherDashboardApprovalContent(
    detail: AttendanceDetailEntity?,
    isLoading: Boolean,
    onUpdateForm: (TeacherDashboardApprovalFormData) -> Unit,
    formData: TeacherDashboardApprovalFormData,
    onSubmit: (TeacherDashboardApprovalFormData) -> Unit,
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
        detail?.let {
            if (!isLoading) {
                DetailSection(it)
            } else {
                DetailLoading()
            }
        }
        FormContent(
            onUpdateForm = onUpdateForm,
            formData = formData
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            text = "Submit"
        ) {
            onSubmit(formData)
        }
    }
}

@Composable
private fun FormContent(
    onUpdateForm: (TeacherDashboardApprovalFormData) -> Unit,
    formData: TeacherDashboardApprovalFormData,
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
            value = formData.note,
            onValueChange = {
                onUpdateForm(
                    formData.copy(
                        note = it
                    )
                )
            },
            title = "Note",
            placeholder = "Enter approval note"
        )
    }
}


@Composable
fun DetailSection(
    detail: AttendanceDetailEntity,
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
        detail.permit?.reason?.let {
            RecordField(
                title = "Reason",
                content = it
            )
        }
        RecordField(
            title = "Duration",
            content = getDuration(detail.permit ?: AttendanceDetailEntity.Permit())
        )
        RecordField(
            title = "Permit Attachment"
        ) {
            AttachmentButton(detail.permit?.attachment ?: "")
        }
        RecordField(
            title = "Note",
            content = detail.permit?.note ?: "-"
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
private fun AttachmentButton(url: String) {
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
    permit: AttendanceDetailEntity.Permit,
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