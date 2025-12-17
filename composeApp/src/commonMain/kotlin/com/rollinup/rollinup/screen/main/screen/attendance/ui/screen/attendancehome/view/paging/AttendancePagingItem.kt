package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.rollinup.component.card.Card
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.date.DateText
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_clock_filled_24

@Composable
fun AttendancePagingItem(
    item: AttendanceByClassEntity,
    onClickAction: () -> Unit,
) {
    Card(
        showAction = true,
        onClickAction = onClickAction
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(itemGap4)
        ) {
            RowData(
                leftContent = {
                    Text(
                        text = item.student.name,
                        style = Style.title,
                        color = theme.bodyText
                    )
                },
                rightContent = {
                    val status = item.attendance?.status ?: AttendanceStatus.NO_DATA
                    Chip(
                        text = status.label,
                        severity = status.severity
                    )
                }
            )
            RowData(
                leftContent = {
                    Text(
                        text = item.student.studentId,
                        style = Style.body,
                        color = theme.bodyText
                    )
                },
                rightContent = {
                    item.attendance?.checkedInAt?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_clock_filled_24),
                                tint = theme.textPrimary,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(2.dp)
                            DateText(
                                dateTime = it.parseToLocalDateTime(TimeZone.UTC),
                                style = Style.body,
                                color = theme.textPrimary
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun AttendancePagingLoading() {
    Card {
        Column(
            verticalArrangement = Arrangement.spacedBy(itemGap4)
        ) {
            RowData(
                leftContent = { ShimmerEffect(120.dp) },
                rightContent = { ShimmerEffect(60.dp) }
            )

            RowData(
                leftContent = { ShimmerEffect(80.dp) },
                rightContent = { ShimmerEffect(60.dp) }
            )
        }
    }
}

@Composable
private fun RowData(
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = verticalAlignment
    ) {
        leftContent()
        rightContent()
    }
}