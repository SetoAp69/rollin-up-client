package com.rollinup.apiservice.model.permit

enum class PermitType(val value: String, val label: String) {
    DISPENSATION("DISPENSATION", "Dispensation"),
    ABSENCE("ABSENCE", "Absence")
    ;

    companion object {
        fun fromValue(value: String): PermitType {
            return entries.find { it.value.equals(value, true) } ?: ABSENCE
        }
    }
}