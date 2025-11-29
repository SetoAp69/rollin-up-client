package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.attendance.AttendanceSummaryEntity
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.record.RecordField
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.theme

@Composable
fun AttendanceByStudentSummary(
    isLoading: Boolean,
    summary: AttendanceSummaryEntity,
) {
    if (isLoading) {
        AttendanceByStudentSummaryLoading()
    } else {
        AttendanceByStudentSummaryContent(summary)
    }
}

@Composable
fun AttendanceByStudentSummaryContent(summary: AttendanceSummaryEntity) {
    Column {
        RowData(
            leftContent = {
                RecordFieldWithNotation(
                    label = "Checked In",
                    color = theme.success,
                    content = summary.checkedIn.toString(),
                )
            },
            rightContent = {
                RecordFieldWithNotation(
                    label = "Absent",
                    color = theme.danger,
                    content = summary.absent.toString(),
                )
            }
        )
        RowData(
            leftContent = {
                RecordFieldWithNotation(
                    label = "Late",
                    color = theme.warning,
                    content = summary.late.toString(),
                )
            },
            rightContent = {
                RecordFieldWithNotation(
                    label = "Sick",
                    color = theme.danger,
                    content = summary.sick.toString(),
                )
            }
        )
        RowData(
            leftContent = {
                RecordFieldWithNotation(
                    label = "Excused",
                    color = theme.warning,
                    content = summary.excused.toString(),
                )
            },
            rightContent = {
                RecordFieldWithNotation(
                    label = "Other",
                    color = theme.danger,
                    content = summary.other.toString(),
                )
            }
        )
    }
}

@Composable
fun AttendanceByStudentSummaryLoading() {
    Column(verticalArrangement = Arrangement.spacedBy(itemGap4)) {
        repeat(3) {
            ShimmerEffect(120.dp)
        }
    }
}

@Composable
private fun RowData(
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        leftContent()
        Spacer(itemGap8)
        rightContent()
    }
}

@Composable
private fun RecordFieldWithNotation(
    label: String,
    color: Color,
    content: String,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(top = itemGap4)
                .size(12.dp)
                .background(color = color, shape = CircleShape)
        )
        Spacer(itemGap4)
        RecordField(
            title = label,
            verticalAlignment = verticalAlignment,
            content = content
        )
    }
}