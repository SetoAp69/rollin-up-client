package com.rollinup.apiservice.model.attendance

import com.rollinup.apiservice.model.permit.PermitType

data class AttendanceDetailEntity(
    val id: String = "",
    val student: User = User(),
    val status: AttendanceStatus = AttendanceStatus.NO_DATA,
    val checkedInAt: String? = null,
    val updatedAt: String = "",
    val createdAt: String = "",
    val longitude: Double? = null,
    val latitude: Double? = null,
    val permit: Permit? = null,
) {
    data class User(
        val id: String = "",
        val studentId: String? = null,
        val name: String = "",
        val xClass: String? = null,
    )

    data class Permit(
        val id: String = "",
        val reason: String? = null,
        val type: PermitType = PermitType.ABSENT,
        val startTime: String = "",
        val endTime: String = "",
        val note: String? = null,
        val attachment: String = "",
        val approvalNote: String? = null,
        val approvedBy: User? = null,
        val approvedAt: String? = null,
    )
}
