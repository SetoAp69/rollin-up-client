package com.rollinup.rollinup.screen.main.screen.attendance.model.attendancebystudent

data class AttendanceByStudentCallback(
    val onRefresh: () -> Unit = {},
    val onGetDetail: (String) -> Unit = {},
    val onUpdateFilter: (AttendanceByStudentFilterData) -> Unit = {},
    val onExportFile: (String) -> Unit = {},
    val onResetMessageState: () -> Unit = {},
)
