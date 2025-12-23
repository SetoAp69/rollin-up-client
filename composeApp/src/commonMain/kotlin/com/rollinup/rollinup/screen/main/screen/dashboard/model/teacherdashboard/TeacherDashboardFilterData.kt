package com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard

import com.rollinup.apiservice.utils.Utils.toJsonString
import com.rollinup.apiservice.data.source.network.model.request.attendance.GetAttendanceListByClassQueryParams
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.common.utils.Utils.now
import com.rollinup.common.utils.Utils.toEpochMilli
import kotlinx.datetime.LocalDate

data class TeacherDashboardFilterData(
    val classX: Int = 0,
    val searchQuery: String = "",
    val status: List<AttendanceStatus> = emptyList(),
) {
    fun toQueryParams() = GetAttendanceListByClassQueryParams(
        search = searchQuery.ifBlank { null },
        status = status.map { it.value }.toJsonString(),
        date = LocalDate.now().toEpochMilli().toString()
    )
}
