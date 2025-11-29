package com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome

import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity

data class AttendanceCallback(
    val onRefresh: () -> Unit = {},
    val onSearch: (String) -> Unit = {},
    val onFilter: (AttendanceFilterData) -> Unit = {},
    val onGetDetail: (AttendanceByClassEntity) -> Unit = {},
)
