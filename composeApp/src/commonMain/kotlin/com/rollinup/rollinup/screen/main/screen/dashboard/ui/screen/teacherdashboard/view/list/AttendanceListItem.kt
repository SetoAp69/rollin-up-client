package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.list

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
import com.rollinup.rollinup.component.date.PermitDateText
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_clock_filled_24

@Composable
fun AttendanceListItem(
    item: AttendanceByClassEntity,
    cb: TeacherDashboardCallback,
    uiState: TeacherDashboardUiState,
    onClickAction: (AttendanceByClassEntity) -> Unit,
) {
    val isSelecting = uiState.itemSelected.isNotEmpty()
    val status = item.attendance?.status ?: AttendanceStatus.NO_DATA

    Card(
        showAction = !isSelecting,
        onClick = { if (isSelecting) cb.onUpdateSelection(item) },
        onLongClick = { if (!isSelecting) cb.onUpdateSelection(item) },
        onClickAction = {
            onClickAction(item)
        },
        backgroundColor = if (uiState.itemSelected.contains(item)) theme.popUpBgSelected else theme.popUpBg
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(itemGap4)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.student.name,
                    style = Style.title,
                    color = theme.bodyText
                )
                Chip(
                    text = status.name,
                    severity = status.severity,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.student.studentId,
                    style = Style.body,
                    color = theme.bodyText
                )
                when (item.attendance?.status) {
                    AttendanceStatus.LATE, AttendanceStatus.ON_TIME -> {
                        item.attendance?.checkedInAt?.let {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_clock_filled_24),
                                    contentDescription = null,
                                    tint = theme.textPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(itemGap4)
                                DateText(
                                    dateTime = it.parseToLocalDateTime(TimeZone.UTC),
                                    color = theme.textPrimary
                                )
                            }
                        }
                    }

                    AttendanceStatus.ABSENT, AttendanceStatus.EXCUSED -> {
                        item.permit?.let {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_clock_filled_24),
                                    contentDescription = null,
                                    tint = theme.textPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(itemGap4)
                                PermitDateText(
                                    start = it.start,
                                    end = it.end,
                                    type = it.type,
                                    color = theme.textPrimary
                                )
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}


@Composable
fun AttendanceListItemLoading() {
    Card {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerEffect(120.dp)
                ShimmerEffect(60.dp)
            }
            Spacer(itemGap4)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerEffect(120.dp)
                ShimmerEffect(80.dp)
            }
        }
    }
}