package com.rollinup.rollinup.screen.main.screen.dashboard.model.studentdashboard

import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.rollinup.component.location.Location

data class StudentDashboardCallback(
    val onRefresh: () -> Unit = {},
    val onShowAttendanceDetail: (String) -> Unit = {},
    val onUpdateLocation: (Location?, Boolean) -> Unit = { _, _ -> },
    val onCheckIn: (MultiPlatformFile, Location) -> Unit = { _, _ -> },
    val onUpdateDateRangeSelected: (List<Long>) -> Unit = {},
    val onUpdateLoginData: (LoginEntity) -> Unit = {},
    val onUpdateTempPhoto: (MultiPlatformFile?) -> Unit = {},
    val onResetMessageState: () -> Unit = {},
)
