package com.rollinup.apiservice.model.attendance

enum class AttendanceStatus(val value: String) {
    CHECKED_IN("checked_in"),
    LATE("late"),
    ABSENT("absent"),
    EXCUSED("excused"),
    APPROVAL_PENDING("approval_pending"),
    NO_DATA("no_data")
    ;

    companion object {
        fun fromValue(value: String): AttendanceStatus {
            return entries.find { it.value.equals(value, true) } ?: NO_DATA
        }
    }
}