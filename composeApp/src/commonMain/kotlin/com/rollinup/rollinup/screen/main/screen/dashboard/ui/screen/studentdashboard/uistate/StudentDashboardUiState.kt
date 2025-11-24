package com.rollinup.rollinup.screen.dashboard.ui.screen.studentdashboard.uistate

import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.attendance.AttendanceSummaryEntity
import com.rollinup.apiservice.model.auth.LoginEntity
import dev.jordond.compass.Location

data class StudentDashboardUiState(
    val isLoadingHeader: Boolean = false,
    val isLoadingDetail: Boolean = false,
    val isLoadingCalendar: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val summary: AttendanceSummaryEntity = AttendanceSummaryEntity(),
    val currentStatus: AttendanceStatus = AttendanceStatus.NO_DATA,
    val attendanceDetail: AttendanceDetailEntity = AttendanceDetailEntity(),
    val currentAttendance: AttendanceByStudentEntity? = null,
    val checkInState: Boolean? = null,
    val user: LoginEntity? = null,
    val attendanceList: List<AttendanceByStudentEntity> = emptyList(),
    val isLocationValid: Boolean? = null,
    val currentLocation: Location? = null,
    val selectedDateRange: List<Long> = emptyList(),
)
