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
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_absent
import rollin_up.composeapp.generated.resources.label_excused
import rollin_up.composeapp.generated.resources.label_late
import rollin_up.composeapp.generated.resources.label_on_time
import rollin_up.composeapp.generated.resources.label_other
import rollin_up.composeapp.generated.resources.label_sick

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
    RowData(
        leftContent = {
            Column {
                RecordFieldWithNotation(
                    label = stringResource(Res.string.label_on_time),
                    color = theme.success,
                    content = summary.checkedIn.toString(),
                )

                RecordFieldWithNotation(
                    label = stringResource(Res.string.label_late),
                    color = theme.warning,
                    content = summary.late.toString(),
                )
                RecordFieldWithNotation(
                    label = stringResource(Res.string.label_excused),
                    color = theme.warning,
                    content = summary.excused.toString(),
                )
            }
        },
        rightContent = {
            Column {
                RecordFieldWithNotation(
                    label = stringResource(Res.string.label_absent),
                    color = theme.danger,
                    content = summary.absent.toString(),
                )
                RecordFieldWithNotation(
                    label = stringResource(Res.string.label_sick),
                    color = theme.danger,
                    content = summary.sick.toString(),
                )
                RecordFieldWithNotation(
                    label = stringResource(Res.string.label_other),
                    color = theme.danger,
                    content = summary.other.toString(),
                )
            }
        }
    )
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