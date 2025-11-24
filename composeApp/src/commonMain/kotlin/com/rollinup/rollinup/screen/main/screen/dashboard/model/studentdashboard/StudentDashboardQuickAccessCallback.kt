package com.rollinup.rollinup.screen.dashboard.model.studentdashboard

data class StudentDashboardQuickAccessCallback(
    val onCreatePermit: () -> Unit,
    val onCheckIn: () -> Unit,
    val onGoToHistory: () -> Unit,
)
