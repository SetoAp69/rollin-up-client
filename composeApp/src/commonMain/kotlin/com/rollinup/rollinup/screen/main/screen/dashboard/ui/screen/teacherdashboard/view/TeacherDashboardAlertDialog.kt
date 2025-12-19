package com.rollinup.rollinup.screen.dashboard.ui.screen.teacherdashboard.view

import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.theme.theme
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24

@Composable
fun SubmitAttendanceEditDialog(
    isShowDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    status: AttendanceStatus,
    initialStatus: AttendanceStatus,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    val message = when {
        initialStatus == AttendanceStatus.ABSENT && status in listOf(
            AttendanceStatus.ON_TIME,
            AttendanceStatus.LATE
        ) -> {
            "This action will update current attendance data and cancel future permit(s), are you sure you want to continue?"
        }

        status == AttendanceStatus.NO_DATA ->{
            "This action will delete current attendance data, are you sure you want to continue?"
        }

        else -> "This action will update current attendance data, are you sure you want to continue?"
    }

    AlertDialog(
        isShowDialog = isShowDialog,
        title = "Edit Attendance Data",
        icon = Res.drawable.ic_info_line_24,
        iconTint = theme.primary,
        content = message,
        onClickCancel = onCancel,
        onDismissRequest = onDismissRequest,
        onClickConfirm = onConfirm,
        btnCancelText = "Cancel",
        btnConfirmText = "Continue"
    )
}