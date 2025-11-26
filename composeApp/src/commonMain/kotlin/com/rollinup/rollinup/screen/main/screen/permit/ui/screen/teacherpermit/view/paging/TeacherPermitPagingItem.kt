package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view.paging

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
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.common.model.Severity
import com.rollinup.common.utils.Utils.toLocalDateTime
import com.rollinup.rollinup.component.card.Card
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.date.DateText
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit.TeacherPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.uistate.TeacherPermitUiState
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_clock_filled_24

@Composable
fun TeacherPermitPagingItem(
    item: PermitByClassEntity,
    uiState: TeacherPermitUiState,
    cb: TeacherPermitCallback,
    onClickAction: (PermitByClassEntity) -> Unit,
) {
    val isSelecting = uiState.itemSelected.isNotEmpty()
    val isSelected = uiState.itemSelected.contains(item)

    Card(
        showAction = true,
        onClick = { if (isSelecting) cb.onUpdateSelection(item) },
        onLongClick = { if (!isSelecting) cb.onUpdateSelection(item) },
        onClickAction = { onClickAction(item) },
        backgroundColor = if (isSelected) theme.popUpBgSelected else theme.popUpBg,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(itemGap4)) {
            ItemDataRow(
                leftContent = {
                    Text(
                        text = item.student.name,
                        color = theme.bodyText,
                        style = Style.title
                    )
                },
                rightContent = {
                    Chip(
                        text = item.type,
                        severity = Severity.SECONDARY
                    )
                }
            )
            ItemDataRow(
                leftContent = {
                    Text(
                        text = item.student.xClass,
                        color = theme.bodyText,
                        style = Style.body
                    )
                },
                rightContent = {
                    if (item.reason == null) {
                        DateText(dateString = item.startTime)
                    }
                }
            )
            if (item.reason != null) {
                ItemDataRow(
                    leftContent = {
                        Text(
                            text = item.reason!!,
                            color = theme.bodyText,
                            style = Style.body
                        )
                    },
                    rightContent = {
                        DateText(dateString = item.startTime)
                    },
                )
            }
            ItemDataRow(
                leftContent = {
                    Row {
                        Icon(
                            painterResource(Res.drawable.ic_clock_filled_24),
                            tint = theme.textPrimary,
                            contentDescription = "duration",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(itemGap4)
                        Text(
                            text = generateDuration(item.startTime, item.endTime),
                            color = theme.bodyText,
                            style = Style.body
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
fun TeacherPermitPagingItemLoading() {
    Card {
        Column(verticalArrangement = Arrangement.spacedBy(itemGap4)) {
            ItemDataRow(
                leftContent = {
                    ShimmerEffect(150.dp)
                },
                rightContent = {
                    ShimmerEffect(60.dp)
                }
            )
            ItemDataRow(
                leftContent = {
                    ShimmerEffect(80.dp)
                },
                rightContent = {
                    ShimmerEffect(120.dp)
                }
            )
            ItemDataRow(
                leftContent = {
                    ShimmerEffect(120.dp)
                },
                rightContent = {
                    ShimmerEffect(80.dp)
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
        rightContent()
    }
}

private fun generateDuration(from: String, to: String): String {
    val dateTimeFrom = from.toLocalDateTime()
    val dateTimeTo = to.toLocalDateTime()

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