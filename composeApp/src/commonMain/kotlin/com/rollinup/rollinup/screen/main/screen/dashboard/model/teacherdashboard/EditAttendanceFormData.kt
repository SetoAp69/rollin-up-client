package com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard

import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.rollinup.component.permitform.model.PermitFormData
import dev.jordond.compass.Coordinates

data class EditAttendanceFormData(
    val status: AttendanceStatus = AttendanceStatus.NO_DATA,
    val studentUserId: String = "",
    val permitFormData: PermitFormData = PermitFormData(),
    val location: Coordinates? = null,
    val checkInTime: Long? = null,
    val checkInTimeError: String? = null,
) {
    fun isValid(): Boolean {
        return listOf(checkInTimeError == null, permitFormData.isValid).all { it }
    }
}
