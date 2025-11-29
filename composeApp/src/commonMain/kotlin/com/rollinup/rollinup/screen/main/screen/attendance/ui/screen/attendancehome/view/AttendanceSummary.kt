package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
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
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme

@Composable
fun AttendanceSummary(
    isLoading: Boolean,
    summary: AttendanceSummaryEntity,
) {
    if (isLoading) {
        AttendanceSummaryLoading()
    } else {
        AttendanceSummaryContent(summary)
    }
}

@Composable
private fun AttendanceSummaryContent(
    summary: AttendanceSummaryEntity,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(itemGap4),
    ) {
        RowData(
            leftContent = {
                AttendanceSummaryItem(
                    title = "Checked In",
                    value = summary.checkedIn.toString(),
                    lineColor = theme.success
                )
            },
            rightContent = {
                AttendanceSummaryItem(
                    title = "Excused",
                    value = summary.excused.toString(),
                    lineColor = theme.danger
                )
            }
        )
        RowData(
            leftContent = {
                AttendanceSummaryItem(
                    title = "Late",
                    value = summary.late.toString(),
                    lineColor = theme.warning
                )
            },
            rightContent = {
                AttendanceSummaryItem(
                    title = "Absent",
                    value = summary.absent.toString(),
                    lineColor = theme.danger
                )
            }
        )
    }
}

@Composable
private fun AttendanceSummaryLoading() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(itemGap4),
    ) {
        RowData(
            leftContent = {
                ShimmerEffect(80.dp)
            },
            rightContent = {
                ShimmerEffect(80.dp)
            }
        )
        RowData(
            leftContent = {
                ShimmerEffect(80.dp)
            },
            rightContent = {
                ShimmerEffect(80.dp)
            }
        )
    }
}


@Composable
private fun AttendanceSummaryItemx(
    title: String,
    value: String,
    lineColor: Color,
) {
    Column(
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = title,
            style = Style.title,
            color = theme.bodyText
        )
        Spacer(itemGap4)
        Text(
            text = value,
            style = Style.headerBold,
            color = lineColor
        )
    }
}

@Composable
private fun RowData(
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        leftContent()
        Spacer(itemGap8)
        rightContent()
    }
}

@Composable
private fun AttendanceSummaryItem(
    title: String,
    lineColor: Color,
    value: String,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(top = itemGap4)
                .size(12.dp)
                .background(color = lineColor, shape = CircleShape)
        )
        Spacer(itemGap4)
        RecordField(
            title = title,
            verticalAlignment = verticalAlignment,
            content = value
        )
    }
}