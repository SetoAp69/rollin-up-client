package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.view.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.permit.PermitByStudentEntity
import com.rollinup.common.model.Severity
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.rollinup.component.card.Card
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme

@Composable
fun StudentPermitItem(
    item: PermitByStudentEntity,
    onClickAction: (PermitByStudentEntity) -> Unit,
) {
    Card(
        showAction = true,
        onClickAction = {
            onClickAction(item)
        },
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(itemGap4)) {
            ItemDataRow(
                leftContent = {
                    Text(
                        style = Style.title,
                        text = generateDuration(item.startTime, item.endTime),
                        color = theme.bodyText
                    )
                },
                rightContent = {
                    Chip(
                        text = item.type.label,
                        severity = Severity.SECONDARY
                    )
                }
            )
            ItemDataRow(
                leftContent = {
                    item.reason?.let {
                        Text(
                            style = Style.body,
                            text = it,
                            color = theme.bodyText
                        )
                    }
                },
                rightContent = {
                    Chip(
                        text = item.approvalStatus.label,
                        severity = item.approvalStatus.severity
                    )
                }
            )
        }
    }
}

@Composable
fun StudentPermitItemLoading() {
    Card(
        showAction = true,
        onClickAction = {},
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(itemGap4)) {
            ItemDataRow(
                leftContent = {
                    ShimmerEffect(120.dp)
                },
                rightContent = {
                    ShimmerEffect(60.dp)
                }
            )
            ItemDataRow(
                leftContent = {
                    ShimmerEffect(60.dp)
                },
                rightContent = {
                    ShimmerEffect(60.dp)
                }
            )
        }
    }
}


@Composable
private fun ItemDataRow(
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
        Spacer(modifier = Modifier.weight(1f))
        rightContent()
    }
}

private fun generateDuration(from: String, to: String): String {
    val dateTimeFrom = from.parseToLocalDateTime()
    val dateTimeTo = to.parseToLocalDateTime()

    return when {
        dateTimeFrom == dateTimeTo -> {
            with(dateTimeFrom) {
                "${date.day} ${month.name.take(3)}"
            }
        }

        dateTimeFrom.date == dateTimeTo.date -> {
            val date = "${dateTimeFrom.date.dayOfWeek.name.take(3)}, ${dateTimeFrom.date.day} ${
                dateTimeFrom.month.name.take(3)
            }"
            val timeFrom = "${dateTimeFrom.hour}:${dateTimeFrom.minute}"
            val timeTo = "${dateTimeTo.hour}:${dateTimeTo.minute}"
            "$date $timeFrom - $timeTo"
        }

        else -> {
            val from = "${dateTimeFrom.dayOfWeek.name.take(3)}, ${dateTimeFrom.date.day} ${
                dateTimeFrom.date.month.name.take(3)
            }"
            val to = "${dateTimeTo.dayOfWeek.name.take(3)}, ${dateTimeTo.date.day} ${
                dateTimeTo.date.month.name.take(3)
            }"
            "$from - $to"
        }

    }
}