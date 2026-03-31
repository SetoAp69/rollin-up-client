package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.attendance.AttendanceSummaryEntity
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.model.getLabel
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.isCompact
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.uistate.StudentDashboardUiState
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_location_invalid_line_24
import rollin_up.composeapp.generated.resources.ic_location_line_24
import rollin_up.composeapp.generated.resources.label_absent
import rollin_up.composeapp.generated.resources.label_excused
import rollin_up.composeapp.generated.resources.label_late
import rollin_up.composeapp.generated.resources.label_on_time
import rollin_up.composeapp.generated.resources.msg_hello

@Composable
fun StudentDashboardHeader(
    uiState: StudentDashboardUiState,
) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = theme.primary,
                spotColor = theme.primary,
            )
            .border(width = 1.dp, color = theme.lineStroke, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(theme.popUpBg)
    ) {
        if (uiState.isLoadingHeader) {
            HeaderLoadingContent()
        } else {
            HeaderContent(
                uiState = uiState
            )
        }
    }
}

@Composable
fun HeaderLoadingContent() {
    Column(
        modifier = Modifier
            .padding(screenPaddingValues)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(itemGap8)
            ) {
                ShimmerEffect(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(50))
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(itemGap4)
                ) {
                    ShimmerEffect(80.dp)
                    ShimmerEffect(120.dp, 24.dp)
                    ShimmerEffect(80.dp)
                }
            }
            ShimmerEffect(60.dp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(itemGap8),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(itemGap8)
            ) {
                ShimmerEffect(42.dp, 42.dp)
                ShimmerEffect(
                    modifier = Modifier
                        .size(itemGap4)
                        .clip(RoundedCornerShape(50))
                )
                ShimmerEffect(42.dp, 42.dp)
                ShimmerEffect(
                    modifier = Modifier
                        .size(itemGap4)
                        .clip(RoundedCornerShape(50))
                )
                ShimmerEffect(42.dp, 42.dp)
            }
        }
    }
}

@Composable
private fun HeaderContent(
    uiState: StudentDashboardUiState,
) {
    Column(
        modifier = Modifier
            .padding(screenPaddingValues)
            .fillMaxWidth()
    ) {
        UserInfoSection(
            userData = uiState.user ?: LoginEntity(),
            attendanceStatus = uiState.currentStatus,
            isLocationValid = uiState.isLocationValid
        )
        SummarySection(
            summary = uiState.summary,
        )
    }
}


@Composable
fun SummarySection(
    summary: AttendanceSummaryEntity,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(itemGap8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SummaryItem(
            amount = summary.checkedIn.toString(),
            title = stringResource(Res.string.label_on_time),
            severity = Severity.SUCCESS
        )
        CircleSpacer()
        SummaryItem(
            amount = summary.late.toString(),
            title = stringResource(Res.string.label_late),
            severity = Severity.WARNING
        )
        CircleSpacer()
        SummaryItem(
            amount = summary.excused.toString(),
            title = stringResource(Res.string.label_excused),
            severity = Severity.WARNING
        )
        CircleSpacer()
        SummaryItem(
            amount = summary.absent.toString(),
            title = stringResource(Res.string.label_absent),
            severity = Severity.DANGER
        )
    }
}

@Composable
private fun CircleSpacer() {
    Box(
        modifier = Modifier
            .size(itemGap4)
            .background(
                color = theme.primary,
                shape = RoundedCornerShape(50)
            )
    )
}

@Composable
private fun SummaryItem(
    amount: String,
    title: String,
    severity: Severity,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = amount,
            style = Style.header,
            color = theme.primary
        )
        Spacer(itemGap4)
        Chip(
            text = title,
            severity = severity
        )
    }
}

@Composable
private fun UserInfoSection(
    userData: LoginEntity,
    isLocationValid: Boolean?,
    attendanceStatus: AttendanceStatus,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(itemGap8)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        color = theme.primary,
                        shape = RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userData.fullName.take(1).ifBlank { "-" },
                    style = Style.header,
                    color = Color.White
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(itemGap4)
            ) {
                Text(
                    text = stringResource(Res.string.msg_hello),
                    color = theme.chipDisabledBg,
                    style = Style.body
                )

                Text(
                    text = userData.fullName.split(" ").firstOrNull()?:"-",
                    color = theme.bodyText,
                    style = Style.popupTitle
                )

                Text(
                    text = userData.classX ?: "-",
                    color = theme.chipDisabledBg,
                    style = Style.body
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(itemGap8)
        ) {
            Chip(
                text = attendanceStatus.getLabel(),
                severity = attendanceStatus.severity
            )
            LocationStatus(
                isLocationValid = isLocationValid
            )
        }

    }
}

@Composable
private fun LocationStatus(
    isLocationValid: Boolean?,
) {
    val locationIcon: DrawableResource
    val iconColor: Color

    when (isLocationValid) {
        true -> {
            locationIcon = Res.drawable.ic_location_line_24
            iconColor = theme.success
        }

        false -> {
            locationIcon = Res.drawable.ic_location_invalid_line_24
            iconColor = theme.danger
        }

        null -> {
            locationIcon = Res.drawable.ic_location_invalid_line_24
            iconColor = theme.chipDisabledBg
        }
    }
    Icon(
        modifier = Modifier.size(if (isCompact) 36.dp else 42.dp),
        painter = painterResource(locationIcon),
        contentDescription = null,
        tint = iconColor
    )

}