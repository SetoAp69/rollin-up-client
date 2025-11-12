package com.rollinup.apiservice.model.attendance

import com.rollinup.apiservice.model.permit.PermitType

data class AttendanceByStudentEntity(
    val id: String = "",
    val status: AttendanceStatus = AttendanceStatus.NO_DATA,
    val date: String = "",
    val checkInTime: String? = null,
    val permit: Permit? = null,
) {
    data class Permit(
        val id: String = "",
        val reason: String? = null,
        val type: PermitType = PermitType.ABSENT,
        val start: String = "",
        val end: String = "",
    )
}
