package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view.paging

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
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.utils.Utils.toLocalDateTime
import com.rollinup.rollinup.component.card.Card
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.date.DateText
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_clock_filled_24

@Composable
fun AttendanceByStudentPagingItem(
    item: AttendanceByStudentEntity,
    onClickItem: (AttendanceByStudentEntity) -> Unit,
) {
    Card(
        onClick = { onClickItem(item) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(itemGap4)) {
            RowData(
                leftContent = {
                    DateText(
                        dateString = item.date,
                        style = Style.title,
                        color = theme.bodyText
                    )
                },
                rightContent = {
                    Chip(
                        text = item.status.label,
                        severity = item.status.severity
                    )
                }
            )
            RowData(
                leftContent = {
                    item.permit?.reason?.let {
                        Text(
                            text = it,
                            color = theme.bodyText,
                            style = Style.body
                        )
                    }
                },
                rightContent = {
                    Row {
                        Icon(
                            painter = painterResource(Res.drawable.ic_clock_filled_24),
                            contentDescription = null,
                            tint = theme.textPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        when (item.status) {
                            AttendanceStatus.LATE, AttendanceStatus.CHECKED_IN -> {
                                DateText(
                                    dateString = item.checkInTime ?: "-",
                                    color = theme.textPrimary
                                )
                            }

                            AttendanceStatus.ABSENT, AttendanceStatus.EXCUSED -> {
                                item.permit?.let {
                                    Text(
                                        text = getDuration(it),
                                        color = theme.textPrimary,
                                        style = Style.body
                                    )
                                }
                            }

                            else -> {}
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun AttendanceByStudentPagingLoading() {
    Card {
        Column(verticalArrangement = Arrangement.spacedBy(itemGap4)) {
            RowData(
                leftContent = {
                    ShimmerEffect(120.dp)
                },
                rightContent = {
                    ShimmerEffect(60.dp)
                }
            )
            RowData(
                leftContent = {
                    ShimmerEffect(60.dp)
                },
                rightContent = {
                    ShimmerEffect(100.dp)
                }
            )
        }
    }
}


private fun getDuration(
    permit: AttendanceByStudentEntity.Permit,
): String {
    val from = permit.start.toLocalDateTime()
    val to = permit.end.toLocalDateTime()

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

@Composable
private fun RowData(
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = verticalAlignment,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        leftContent()
        rightContent()
    }
}