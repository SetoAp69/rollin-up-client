package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.uistate

import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByClassQueryParams
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.attendance.AttendanceDetailEntity
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.apiservice.model.attendance.AttendanceSummaryEntity
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.utils.Utils.toJsonString
import com.rollinup.common.model.OptionData
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceFilterData
import kotlinx.datetime.LocalDate

data class AttendanceUiState(
    val isLoading: Boolean = false,
    val isLoadingSummary: Boolean = false,
    val isLoadingDetail: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val user: LoginEntity = LoginEntity(),
    val filterData: AttendanceFilterData = AttendanceFilterData(),
    val searchQuery: String = "",
    val attendanceDetail: AttendanceDetailEntity = AttendanceDetailEntity(),
    val summary: AttendanceSummaryEntity = AttendanceSummaryEntity(),
    val attendanceList: List<AttendanceByClassEntity> = emptyList(),
    val exportDateRanges: List<LocalDate> = emptyList(),
    val exportState: Boolean? = null,
) {
    val queryParams
        get() = GetAttendanceListByClassQueryParams(
            search = searchQuery.ifBlank { null },
            status = filterData.status.toJsonString(),
            date = filterData.date?.toString()
        )

    val statusOptions
        get() = AttendanceStatus.entries.map {
            OptionData(it.label, it)
        }
}
