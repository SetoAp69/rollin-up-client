package com.rollinup.rollinup.screen.dashboard.model.teacherdashboard

data class TeacherDashboardApprovalFormData(
    val id: List<String> = emptyList(),
    val isApproved: Boolean? = null,
    val note: String = "",

    val isApprovedError: String? = null,
)
