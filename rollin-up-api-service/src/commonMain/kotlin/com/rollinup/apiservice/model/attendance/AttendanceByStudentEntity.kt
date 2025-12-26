package com.rollinup.apiservice.model.attendance

import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import kotlinx.datetime.LocalDate

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

    val localDate
        get() = LocalDate.parse(date)

}
