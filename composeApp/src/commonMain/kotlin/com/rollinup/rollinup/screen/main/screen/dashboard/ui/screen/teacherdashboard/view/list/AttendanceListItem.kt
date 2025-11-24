package com.rollinup.rollinup.screen.dashboard.ui.screen.teacherdashboard.view.list

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
import com.rollinup.common.utils.Utils.toLocalDateTime
import com.rollinup.rollinup.component.card.Card
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState
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
        onLongClick = { if (isSelecting.not()) cb.onUpdateSelection(item) },
        onClickAction = {
            onClickAction(item)
        },
        backgroundColor = if (uiState.itemSelected.contains(item)) theme.popUpBgSelected else theme.popUpBg
    ) {
        Column {
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
                item.attendance?.checkedInAt?.let {
                    Row {
                        Icon(
                            painter = painterResource(Res.drawable.ic_clock_filled_24),
                            tint = theme.textPrimary,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(2.dp)
                        Text(
                            text = it.toLocalDateTime().time.toString(),
                            color = theme.textPrimary,
                            style = Style.body
                        )
                    }
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