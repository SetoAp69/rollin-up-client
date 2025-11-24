package com.rollinup.apiservice.model.attendance

import com.rollinup.common.model.Severity

enum class AttendanceStatus(val value: String, val label: String, val severity: Severity) {
    CHECKED_IN("checked_in", "Checked In", Severity.SUCCESS),
    LATE("late", "Late", Severity.WARNING),
    ABSENT("absent", "Absent", Severity.DANGER),
    EXCUSED("excused", "Excused", Severity.WARNING),
    APPROVAL_PENDING("approval_pending", "Approval Pending", Severity.WARNING),
    NO_DATA("no_data", "No Data", Severity.DISABLED)
    ;

    companion object {
        fun fromValue(value: String): AttendanceStatus {
            return entries.find { it.value.equals(value, true) } ?: NO_DATA
        }
    }
}