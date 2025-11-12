package com.rollinup.apiservice.model.common

enum class Gender(val value: String, val label: String) {
    MALE(
        value = "M",
        label = "Male"
    ),
    FEMALE(
        value = "F",
        label = "Female"
    )
    ;

    companion object {
        fun fromValue(value: String): Gender {
            return entries.find { it.value.equals(value, true) } ?: MALE
        }
    }
}