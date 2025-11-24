package com.rollinup.apiservice.model.attendance

data class DashboardDataEntity(
    val attendanceStatus: AttendanceStatus,
    val summary: AttendanceSummaryEntity,
)
