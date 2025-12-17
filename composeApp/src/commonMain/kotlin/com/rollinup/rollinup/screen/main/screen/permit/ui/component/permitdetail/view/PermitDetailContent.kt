package com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitDetailEntity
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.date.DateFormatter
import com.rollinup.rollinup.component.date.DateText
import com.rollinup.rollinup.component.date.DateTextFormat
import com.rollinup.rollinup.component.imageview.ImageView
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.record.RecordField
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import kotlinx.datetime.TimeZone

@Composable
fun PermitDetailDialogContent(
    detail: PermitDetailEntity,
    isLoading: Boolean,
) {
    if (isLoading) {
        PermitDetailLoading()
    } else {
        PermitDetailContent(detail)
    }
}

@Composable
fun PermitDetailContent(detail: PermitDetailEntity) {
    Column {
        HeaderSection(detail)
        Spacer(12.dp)
        DataRecordSection(detail)
    }
}

@Composable
fun PermitDetailLoading() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ShimmerEffect(150.dp, 24.dp)
        Spacer(itemGap8)
        repeat(6) {
            ShimmerEffect(200.dp, 18.dp)
            Spacer(itemGap8)
        }
    }
}

@Composable
private fun HeaderSection(detail: PermitDetailEntity) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = detail.type.label,
            style = Style.headerBold,
            color = theme.primary
        )
    }
}

@Composable
private fun DataRecordSection(
    detail: PermitDetailEntity,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(itemGap4),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        RecordField(
            title = "Name",
            content = detail.student.name
        )
        RecordField(
            title = "Student Id",
            content = detail.student.studentId ?: "-"
        )
        RecordField(
            title = "Class",
            content = detail.student.xClass ?: "-"
        )
        RecordField(
            title = "Status",
            verticalAlignment = Alignment.CenterVertically
        ) {
            Chip(
                text = detail.approvalStatus.label,
                severity = detail.approvalStatus.severity
            )
        }
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
            title = "Note",
            content = detail.note ?: "-"
        )

        RecordField(
            title = "Created at",
            content = {
                DateText(
                    dateTime = detail.createdAt.parseToLocalDateTime(TimeZone.currentSystemDefault())
                )
            }
        )

        RecordField("Attachment") {
            AttachmentButton(
                url = detail.attachment
            )
        }
        ApprovalSection(detail)
    }
}

@Composable
private fun ApprovalSection(
    detail: PermitDetailEntity,
) {
    if (detail.approvalStatus != ApprovalStatus.APPROVAL_PENDING) {
        RecordField(
            title = "Approved at",
        ) {
            detail.approvedAt?.let {
                DateText(
                    dateTime = it.parseToLocalDateTime(TimeZone.currentSystemDefault()),
                    color = theme.textPrimary
                )
            } ?: Text(
                text = "-",
                style = Style.body,
                color = theme.bodyText
            )
        }
        RecordField(
            title = "Approved by",
            content = detail.approvedBy?.name ?: "-"
        )
        RecordField(
            title = "Approval Note",
            content = detail.approvalNote ?: "-"
        )
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
        modifier = Modifier
            .clickable(url.isNotBlank()) {
                showImage = true
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
    val from = permit.startTime.parseToLocalDateTime(TimeZone.UTC)
    val to = permit.endTime.parseToLocalDateTime(TimeZone.UTC)

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
                DateFormatter.formatDateTime(from, DateTextFormat.DATE)
            } else {
                return DateFormatter.formatDateRange(from.date, to.date)
            }
        }
    }
}