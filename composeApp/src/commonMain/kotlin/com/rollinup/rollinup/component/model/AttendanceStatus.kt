package com.rollinup.rollinup.component.model

import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_absent
import rollin_up.composeapp.generated.resources.label_approval_pending
import rollin_up.composeapp.generated.resources.label_excused
import rollin_up.composeapp.generated.resources.label_late
import rollin_up.composeapp.generated.resources.label_no_data
import rollin_up.composeapp.generated.resources.label_on_time

@Composable
fun AttendanceStatus.getLabel(): String {
    val res = when (this) {
        AttendanceStatus.ON_TIME -> Res.string.label_on_time
        AttendanceStatus.LATE -> Res.string.label_late
        AttendanceStatus.ABSENT -> Res.string.label_absent
        AttendanceStatus.EXCUSED -> Res.string.label_excused
        AttendanceStatus.APPROVAL_PENDING -> Res.string.label_approval_pending
        AttendanceStatus.NO_DATA -> Res.string.label_no_data
    }
    return stringResource(res)
}