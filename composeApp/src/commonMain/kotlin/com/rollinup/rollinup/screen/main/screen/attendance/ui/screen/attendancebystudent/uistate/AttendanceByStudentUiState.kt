package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.uistate

import com.rollinup.apiservice.Utils.toJsonString
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByStudentQueryParams
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.attendance.AttendanceSummaryEntity
import com.rollinup.apiservice.model.user.UserDetailEntity
import com.rollinup.rollinup.component.model.OptionData

data class AttendanceByStudentUiState(
    val isLoading: Boolean = false,
    val isLoadingProfile: Boolean = false,
    val isLoadingDetail: Boolean = false,
    val studentUserId: String = "",
    val student: UserDetailEntity = UserDetailEntity(),
    val isLoadingSummary: Boolean = false,
    val summary: AttendanceSummaryEntity = AttendanceSummaryEntity(),
    val statusSelected: List<AttendanceStatus> = emptyList(),
    val attendanceList: List<AttendanceByStudentEntity> = emptyList(),
    val attendanceDetail: AttendanceDetailEntity = AttendanceDetailEntity(),
) {
    val queryParams
        get() = GetAttendanceListByStudentQueryParams(
            status = statusSelected.map { it.value }.toJsonString()
        )

    val statusOptions
        get() = AttendanceStatus.entries
            .filter { it != AttendanceStatus.NO_DATA }
            .map { OptionData(it.label, it) }
}
