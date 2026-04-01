package com.rollinup.apiservice.model.common

enum class Role(val value: String, val labelString: String) {
    ADMIN("admin", "Admin"),
    STUDENT("student", "Student"),
    TEACHER("teacher", "Teacher"),
    UNKNOWN("unknown", "Unknown")
    ;

    companion object {
        fun fromValue(value: String): Role {
            return entries.find { it.value.equals(value, true) } ?: UNKNOWN
        }
    }
}