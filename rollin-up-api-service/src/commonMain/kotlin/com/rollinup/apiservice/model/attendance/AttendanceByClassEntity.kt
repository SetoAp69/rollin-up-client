package com.rollinup.apiservice.model.attendance

import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.utils.Utils.parseToLocalDateTime
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
        val type: PermitType = PermitType.ABSENCE,
        val start: String = "",
        val end: String = "",
    ) {
        val durationString: String
            get() {
                val from = start.parseToLocalDateTime()
                val to = end.parseToLocalDateTime()

                return when (type) {
                    PermitType.DISPENSATION -> {
                        "${from.time} - ${to.time}"
                    }

                    PermitType.ABSENCE -> {
                        val fromDate = from.date
                        val toDate = to.date

                        if (fromDate == toDate) {
                            "${from.date}"
                        } else {
                            "${from.date} - ${to.date}"

                        }
                    }
                }
            }
    }


}
