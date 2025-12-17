package com.rollinup.rollinup.screen.main.screen.dashboard.model.studentdashboard

data class StudentDashboardQuickAccessCallback(
    val onCreatePermit: () -> Unit,
    val onCheckIn: () -> Unit,
    val onGoToPermitHistory: () -> Unit,
    val onGoToAttendanceHistory:()->Unit,
)
