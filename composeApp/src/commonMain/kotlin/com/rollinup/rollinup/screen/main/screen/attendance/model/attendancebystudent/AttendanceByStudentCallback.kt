package com.rollinup.rollinup.screen.main.screen.attendance.model.attendancebystudent

import com.rollinup.apiservice.model.attendance.AttendanceStatus

data class AttendanceByStudentCallback(
    val onSelectStatus: (List<AttendanceStatus>) -> Unit = {},
    val onRefresh: () -> Unit = {},
    val onGetDetail:(String)->Unit = {}
)
