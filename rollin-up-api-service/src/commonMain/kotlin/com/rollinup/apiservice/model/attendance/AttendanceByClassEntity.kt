package com.rollinup.apiservice.model.attendance

import com.rollinup.apiservice.model.permit.PermitType
import kotlinx.serialization.Serializable

data class AttendanceByClassEntity(
    val student: Student = Student(),
    val attendance: Attendance? = null,
    val permit: Permit? = null,
) {
    data class Student(
        val id: String = "",
        val name: String = "",
        val studentId: String = "",
    )

    @Serializable
    data class Attendance(
        val id: String = "",
        val checkedInAt: String? = null,
        val status: AttendanceStatus = AttendanceStatus.NO_DATA,
        val date: String = "",
    )

    data class Permit(
        val id: String = "",
        val reason: String? = null,
        val type: PermitType = PermitType.ABSENT,
        val start: String = "",
        val end: String = "",
    )

}
