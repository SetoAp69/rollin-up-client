package com.rollinup.apiservice.model.attendance

data class AttendanceSummaryEntity(
    val checkedIn: Long = 0L,
    val late: Long = 0L,
    val excused: Long = 0L,
    val approvalPending: Long = 0L,
    val absent: Long = 0L,
    val sick: Long = 0L,
    val other: Long = 0L,
)
