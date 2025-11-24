package com.rollinup.rollinup.screen.dashboard.model.studentdashboard

import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.MultiPlatformFile
import dev.jordond.compass.Location

data class StudentDashboardCallback(
    val onRefresh: () -> Unit = {},
    val onShowAttendanceDetail: (String) -> Unit = {},
    val onUpdateLocationValid: (Boolean) -> Unit = {},
    val onCheckIn: (MultiPlatformFile, Location) -> Unit = { _, _ -> },
    val onUpdateDateRangeSelected: (List<Long>) -> Unit = {},
    val onUpdateLoginData:(LoginEntity)->Unit = {}
)
