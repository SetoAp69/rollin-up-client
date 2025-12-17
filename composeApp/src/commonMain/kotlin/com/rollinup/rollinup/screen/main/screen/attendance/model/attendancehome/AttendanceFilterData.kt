package com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome

import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.common.utils.Utils.toEpochMillis
import com.rollinup.rollinup.component.utils.getCurrentDateAsList

data class AttendanceFilterData(
    val status: List<AttendanceStatus> = emptyList(),
    val date:Long? = getCurrentDateAsList().firstOrNull()?.toEpochMillis(),
)
