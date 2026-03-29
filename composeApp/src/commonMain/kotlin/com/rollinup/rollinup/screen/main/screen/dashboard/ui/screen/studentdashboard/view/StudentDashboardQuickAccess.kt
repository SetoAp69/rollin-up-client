package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.screen.dashboard.ui.component.DashBoardButton
import com.rollinup.rollinup.screen.main.screen.dashboard.model.studentdashboard.StudentDashboardQuickAccessCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.uistate.StudentDashboardUiState
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_location_circled_bottom_line_24
import rollin_up.composeapp.generated.resources.ic_mail_open_line_24
import rollin_up.composeapp.generated.resources.ic_mail_plus_line_24
import rollin_up.composeapp.generated.resources.label_attendance_history
import rollin_up.composeapp.generated.resources.label_check_in
import rollin_up.composeapp.generated.resources.label_create_permit
import rollin_up.composeapp.generated.resources.label_permit
import rollin_up.composeapp.generated.resources.label_quick_access

@Composable
fun StudentDashboardQuickAccess(
    uiState: StudentDashboardUiState,
    cb: StudentDashboardQuickAccessCallback,
) {
    TextFieldTitle(
        title = stringResource(Res.string.label_quick_access),
        textStyle = Style.label
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(itemGap8),
            verticalArrangement = Arrangement.spacedBy(itemGap8),
            itemVerticalAlignment = Alignment.CenterVertically,
        ) {
            DashBoardButton(
                text = stringResource(Res.string.label_create_permit),
                icon = Res.drawable.ic_mail_plus_line_24,
                isEnabled = true,
                onClick = cb.onCreatePermit
            )
            DashBoardButton(
                text = stringResource(Res.string.label_permit),
                icon = Res.drawable.ic_mail_open_line_24,
                isEnabled = true,
                onClick = cb.onGoToPermitHistory
            )
            DashBoardButton(
                text = stringResource(Res.string.label_check_in),
                icon = Res.drawable.ic_location_circled_bottom_line_24,
                isEnabled = uiState.isLocationValid == true && uiState.currentStatus == AttendanceStatus.NO_DATA,
                onClick = cb.onCheckIn
            )
            DashBoardButton(
                text = stringResource(Res.string.label_attendance_history),
                icon = Res.drawable.ic_location_circled_bottom_line_24,
                isEnabled = true,
                onClick = cb.onGoToAttendanceHistory
            )
        }
    }
}