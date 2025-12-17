package com.rollinup.rollinup.screen.main.screen.dashboard.model.studentdashboard

import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.MultiPlatformFile
import dev.jordond.compass.Coordinates
import dev.jordond.compass.Location

data class StudentDashboardCallback(
    val onRefresh: () -> Unit = {},
    val onShowAttendanceDetail: (String) -> Unit = {},
    val onUpdateLocation: (Location?, Coordinates, Double) -> Unit = {_,_,_->},
    val onCheckIn: (MultiPlatformFile, Location) -> Unit = { _, _ -> },
    val onUpdateDateRangeSelected: (List<Long>) -> Unit = {},
    val onUpdateLoginData:(LoginEntity)->Unit = {}
)
