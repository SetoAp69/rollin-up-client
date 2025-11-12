package com.rollinup.apiservice.model.permit

enum class PermitType(val value: String) {
    DISPENSATION("DISPENSATION"),
    ABSENT("ABSENT")
    ;

    companion object {
        fun fromValue(value: String): PermitType {
            return entries.find { it.value.equals(value, true) } ?: ABSENT
        }
    }
}