package com.rollinup.rollinup.component.attendancedetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.rollinup.component.date.DateFormatter
import com.rollinup.rollinup.component.date.DateText
import com.rollinup.rollinup.component.date.DateTextFormat
import com.rollinup.rollinup.component.dialog.Dialog
import com.rollinup.rollinup.component.imageview.ImageView
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.record.RecordField
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getScreenHeight
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_clock_filled_24
import rollin_up.composeapp.generated.resources.ic_user_check_fill_24
import rollin_up.composeapp.generated.resources.ic_user_cross_fill_24
import rollin_up.composeapp.generated.resources.label_attachment
import rollin_up.composeapp.generated.resources.label_class
import rollin_up.composeapp.generated.resources.label_created_at
import rollin_up.composeapp.generated.resources.label_duration
import rollin_up.composeapp.generated.resources.label_name
import rollin_up.composeapp.generated.resources.label_permit_attachment
import rollin_up.composeapp.generated.resources.label_reason
import rollin_up.composeapp.generated.resources.label_student_id
import rollin_up.composeapp.generated.resources.label_view_attachment

@Composable
fun AttendanceDetailDialog(
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    isLoading: Boolean,
    detail: AttendanceDetailEntity,
) {
    val maxHeight = getScreenHeight() * 0.5f

    Dialog(
        showDialog = showDialog,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .heightIn(max = maxHeight)
            .padding(screenPadding * 2)
    ) {
        if (isLoading) {
            AttendanceDetailLoading()
        } else {
            AttendanceDetailContent(detail)
        }
    }
}

@Composable
fun AttendanceDetailLoading() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShimmerEffect(42.dp, 42.dp)
            Spacer(itemGap8)
            ShimmerEffect(60.dp, 24.dp)
        }
        Spacer(itemGap8)
        Column() {
            repeat(5) {
                ShimmerEffect(200.dp, 18.dp)
                Spacer(itemGap8)
            }
        }
    }
}

@Composable
fun AttendanceDetailContent(
    detail: AttendanceDetailEntity,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AttendanceDetailHeaderSection(detail)
        AttendanceDetailRecordSection(detail)
    }
}

@Composable
private fun AttendanceDetailHeaderSection(
    detail: AttendanceDetailEntity,
) {
    val icon: DrawableResource
    val tint: Color

    when (detail.status) {
        AttendanceStatus.ON_TIME -> {
            tint = theme.success
            icon = Res.drawable.ic_user_check_fill_24
        }

        AttendanceStatus.LATE -> {
            tint = theme.warning
            icon = Res.drawable.ic_user_check_fill_24
        }

        AttendanceStatus.ABSENT -> {
            tint = theme.danger
            icon = Res.drawable.ic_user_cross_fill_24
        }

        AttendanceStatus.EXCUSED -> {
            tint = theme.warning
            icon = Res.drawable.ic_user_cross_fill_24
        }

        AttendanceStatus.APPROVAL_PENDING -> {
            tint = theme.warning
            icon = Res.drawable.ic_user_cross_fill_24
        }

        AttendanceStatus.NO_DATA -> {
            tint = theme.danger
            icon = Res.drawable.ic_user_cross_fill_24
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(itemGap4)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(42.dp)
        )
        Text(
            text = detail.status.label,
            color = tint,
            style = Style.headerBold
        )
        detail.checkedInAt?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(Res.drawable.ic_clock_filled_24),
                    tint = theme.textPrimary,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(itemGap4)
                DateText(
                    dateTime = it.parseToLocalDateTime(TimeZone.UTC),
                    color = theme.textPrimary,
                )
            }
        }
    }
}

@Composable
private fun AttendanceDetailRecordSection(
    detail: AttendanceDetailEntity,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(itemGap4)
    ) {
        RecordField(
            title = stringResource(Res.string.label_name),
            content = detail.student.name
        )
        if (detail.status != AttendanceStatus.NO_DATA) {
            RecordField(
                title = stringResource(Res.string.label_student_id),
                content = detail.student.studentId ?: "-"
            )
            RecordField(
                title = stringResource(Res.string.label_class),
                content = detail.student.xClass ?: "-"
            )
        }
        detail.attachment?.let {
            RecordField(
                title = stringResource(Res.string.label_attachment),
            ) {
                AttachmentButton(it)
            }
        }
        detail.permit?.let { permit ->
            RecordField(
                title =  stringResource(Res.string.label_duration),
                content = getDuration(permit)
            )
            RecordField(
                title =  stringResource(Res.string.label_permit_attachment)
            ) {
                AttachmentButton(permit.attachment)
            }
            permit.reason?.let {
                RecordField(
                    title =  stringResource(Res.string.label_reason),
                    content = it
                )
            }
        }
        RecordField(
            title =  stringResource(Res.string.label_created_at),
        ) {
            DateText(detail.createdAt)
        }
    }
}

@Composable
private fun AttachmentButton(
    url: String,
) {
    var showImage by remember { mutableStateOf(false) }
    val text = if (url.isBlank()) "-" else stringResource(Res.string.label_view_attachment)

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