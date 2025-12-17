package com.rollinup.rollinup.screen.main.screen.attendance.model.attendancebystudent

data class AttendanceByStudentFilterData(
    val dateRange:List<Long> = emptyList(),
    val status:List<String> = emptyList()
)
