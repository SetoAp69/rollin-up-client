package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.TextFieldTitle
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.screen.dashboard.ui.component.DashBoardButton
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_mail_line_24
import rollin_up.composeapp.generated.resources.ic_user_check_line_24
import rollin_up.composeapp.generated.resources.ic_user_line_24
import rollin_up.composeapp.generated.resources.label_permit
import rollin_up.composeapp.generated.resources.label_quick_access
import rollin_up.composeapp.generated.resources.label_student_attendance
import rollin_up.composeapp.generated.resources.label_student_center

@Composable
fun TeacherDashboardQuickAccess(
    onPermit: () -> Unit,
    onStudent: () -> Unit,
    onStudentAttendance: () -> Unit,
) {
    TextFieldTitle(
        title = stringResource(Res.string.label_quick_access),
        textStyle = Style.label
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(itemGap8),
            horizontalArrangement = Arrangement.spacedBy(itemGap8),
            itemVerticalAlignment = Alignment.CenterVertically,
        ) {
            DashBoardButton(
                text = stringResource(Res.string.label_permit),
                icon = Res.drawable.ic_mail_line_24,
                onClick = onPermit,
            )
            DashBoardButton(
                text = stringResource(Res.string.label_student_attendance),
                icon = Res.drawable.ic_user_check_line_24,
                onClick = onStudentAttendance
            )
            DashBoardButton(
                text = stringResource(Res.string.label_student_center),
                icon = Res.drawable.ic_user_line_24,
                onClick = onStudent
            )
        }
    }
}