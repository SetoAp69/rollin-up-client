package com.rollinup.rollinup.screen.dashboard.ui.screen.studentdashboard.view

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
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.isCompact
import com.rollinup.rollinup.screen.dashboard.ui.screen.studentdashboard.uistate.StudentDashboardUiState
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_eye_close_line_24
import rollin_up.composeapp.generated.resources.ic_eye_eye_open_line
import rollin_up.composeapp.generated.resources.ic_location_invalid_line_24
import rollin_up.composeapp.generated.resources.ic_location_line_24

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
            attendanceStatus = uiState.currentAttendance?.status ?: AttendanceStatus.NO_DATA
        )
        SummarySection(
            summary = uiState.summary,
            isLocationValid = uiState.isLocationValid == true
        )
    }
}

@Composable
fun SummarySection(
    summary: AttendanceSummaryEntity,
    isLocationValid: Boolean?,
) {
    val locationIcon: DrawableResource
    val iconColor: Color

    when(isLocationValid){
        true ->{
            locationIcon = Res.drawable.ic_location_line_24
            iconColor = theme.success
        }
        false ->{
            locationIcon = Res.drawable.ic_location_invalid_line_24
            iconColor = theme.danger
        }
        null ->{
            locationIcon = Res.drawable.ic_location_invalid_line_24
            iconColor = theme.chipDisabledBg
        }
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
            SummaryItem(
                amount = summary.checkedIn.toString(),
                title = "Attended"
            )
            CircleSpacer()
            SummaryItem(
                amount = summary.absent.toString(),
                title = "Absent"
            )
            CircleSpacer()
            SummaryItem(
                amount = summary.excused.toString(),
                title = "Excused"
            )
        }
        Icon(
            modifier = Modifier.size(if (isCompact) 36.dp else 42.dp),
            painter = painterResource(locationIcon),
            contentDescription = null,
            tint = iconColor
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
        Text(
            text = title,
            style = Style.small,
            color = theme.bodyText
        )
    }
}

@Composable
private fun UserInfoSection(
    userData: LoginEntity,
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
                    text = userData.firstName.take(1).ifBlank { "-" },
                    style = Style.header,
                    color = Color.White
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(itemGap4)
            ) {
                Text(
                    text = "Hello",
                    color = theme.chipDisabledBg,
                    style = Style.body
                )

                Text(
                    text = userData.fullName,
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
        Chip(
            text = attendanceStatus.value,
            severity = attendanceStatus.severity
        )

    }
}