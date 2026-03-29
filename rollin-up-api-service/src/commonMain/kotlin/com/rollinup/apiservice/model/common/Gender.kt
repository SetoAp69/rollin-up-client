package com.rollinup.apiservice.model.common

enum class Gender(val value: String, val labelString: String) {
    MALE(
        value = "M",
        labelString = "Male"
    ),
    FEMALE(
        value = "F",
        labelString = "Female"
    )
    ;

    companion object {
        fun fromValue(value: String): Gender {
            return entries.find { it.value.equals(value, true) } ?: MALE
        }
    }
}