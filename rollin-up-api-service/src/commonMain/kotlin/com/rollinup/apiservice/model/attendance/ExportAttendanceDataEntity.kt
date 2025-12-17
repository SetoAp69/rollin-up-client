package com.rollinup.apiservice.model.attendance

import kotlinx.datetime.LocalDate

data class ExportAttendanceDataEntity(
    val sDateRange: List<String> = emptyList(),
    val data: List<Data> = emptyList(),
) {
    val dateRange
        get() = sDateRange.map { LocalDate.parse(it) }

    data class Data(
        val fullName: String = "",
        val classX: String = "",
        val studentId: String = "",
        val dataPerDate: List<AttendanceRecord> = emptyList(),
    ) {
        data class AttendanceRecord(
            val sDate: String = "",
            val status: String = "",
        ) {
            val date
                get() = LocalDate.parse(sDate)
        }
    }
}