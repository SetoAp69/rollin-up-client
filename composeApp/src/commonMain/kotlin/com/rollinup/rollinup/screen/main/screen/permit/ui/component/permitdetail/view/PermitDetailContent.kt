package com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.apiservice.model.permit.PermitDetailEntity
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.utils.Utils.toFormattedString
import com.rollinup.common.utils.Utils.toLocalDateTime
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.record.RecordField
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme

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
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ShimmerEffect(150.dp, 24.dp)
            ShimmerEffect(60.dp, 24.dp)
            Spacer(itemGap8)
        }
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
private fun HeaderSection(detail: PermitDetailEntity) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = detail.type.label,
            style = Style.headerBold,
            color = theme.primary
        )
        Chip(
            text = detail.approvalStatus.label,
            severity = detail.approvalStatus.severity
        )
    }
}

@Composable
private fun DataRecordSection(
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
            title = "Student Id",
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
            title = "Note",
            content = detail.note ?: "-"
        )

        RecordField(
            title = "Created at",
            content = detail.createdAt
        )

        RecordField("Attachment") {
            Text(
                text = "View Attachment",
                color = theme.textPrimary,
                style = Style.title,
                modifier = Modifier.clickable {
                    //TODO add show files or pic
                }
            )
        }
        ApprovalSection(detail)
    }
}

@Composable
fun ApprovalSection(
    detail: PermitDetailEntity,
) {
    if (detail.approvalStatus != ApprovalStatus.APPROVAL_PENDING) {
        RecordField(
            title = "Approved at",
            content = detail.approvedAt?.toLocalDateTime()?.toString() ?: "-"
        )
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

private fun getDuration(
    permit: PermitDetailEntity,
): String {
    val from = permit.startTime.toLocalDateTime()
    val to = permit.endTime.toLocalDateTime()

    return when (
        permit.type
    ) {
        PermitType.DISPENSATION -> {
            "${from.time} - ${to.time}"
        }

        PermitType.ABSENT -> {
            val fromDate = from.date.toFormattedString()
            val toDate = to.date.toFormattedString()

            if (fromDate == toDate) {
                "${from.time}"
            } else {
                "${from.time} - ${to.time}"

            }
        }
    }
}