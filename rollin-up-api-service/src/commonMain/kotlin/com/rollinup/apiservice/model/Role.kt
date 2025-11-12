package com.rollinup.apiservice.model

enum class Role(val value: String) {
    ADMIN("admin"),
    STUDENT("student"),
    TEACHER("teacher"),
    UNKNOWN("unknown")
    ;

    companion object {
        fun fromValue(value: String): Role {
            return entries.find { it.value.equals(value, true) } ?: UNKNOWN
        }
    }
}