package com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome

import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import kotlinx.datetime.LocalDate

data class AttendanceCallback(
    val onRefresh: () -> Unit = {},
    val onSearch: (String) -> Unit = {},
    val onFilter: (AttendanceFilterData) -> Unit = {},
    val onGetDetail: (AttendanceByClassEntity) -> Unit = {},
    val onExportFile: (String) -> Unit = {},
    val onUpdateExportDateRange: (List<LocalDate>) -> Unit = {},
    val onResetMessageState: () -> Unit = {},
)
