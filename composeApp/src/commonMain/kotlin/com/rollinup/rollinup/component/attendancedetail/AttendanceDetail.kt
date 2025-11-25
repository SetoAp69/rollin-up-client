package com.rollinup.rollinup.component.attendancedetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.utils.Utils.toLocalDateTime
import com.rollinup.rollinup.component.dialog.Dialog
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.record.RecordField
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.component.utils.getScreenHeight
import com.rollinup.rollinup.component.utils.getScreenWidth
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_clock_filled_24
import rollin_up.composeapp.generated.resources.ic_user_check_fill_24
import rollin_up.composeapp.generated.resources.ic_user_cross_fill_24

@Composable
fun AttendanceDetailDialog(
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    isLoading: Boolean,
    detail: AttendanceDetailEntity,
) {
    val maxWidth = if (getPlatform().isMobile()) getScreenWidth() * 0.75f else 400.dp
    val maxHeight = getScreenHeight() * 0.5f

    Dialog(
        showDialog = showDialog,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .sizeIn(maxHeight = maxHeight, maxWidth = maxWidth)
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
fun AttendanceDetailContent(
    detail: AttendanceDetailEntity,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
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
        AttendanceStatus.CHECKED_IN -> {
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
                Text(
                    text = it.toLocalDateTime().time.toString(),
                    style = Style.title,
                    color = theme.textPrimary
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
        detail.permit?.let { permit ->
            RecordField(
                title = "Duration",
                content = getDuration(permit)
            )
            RecordField(
                title = "Permit Attachment"
            ) {
                Text(
                    text = "View Attachment",
                    color = theme.textPrimary,
                    style = Style.title,
                    modifier = Modifier
                        .clickable {
                            //TODO add file download or show file handler
                        }
                )
            }
            permit.reason?.let {
                RecordField(
                    title = "Reason",
                    content = it
                )
            }
        }
        RecordField(
            title = "Created at",
            content = detail.createdAt
        )
    }
}

private fun getDuration(
    permit: AttendanceDetailEntity.Permit,
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