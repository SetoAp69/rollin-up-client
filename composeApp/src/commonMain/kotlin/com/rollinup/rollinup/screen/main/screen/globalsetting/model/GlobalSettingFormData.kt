package com.rollinup.rollinup.screen.main.screen.globalsetting.model

data class GlobalSettingFormData(
    val attendanceStart: Long? = null,
    val attendanceEnd: Long? = null,
    val schoolStart: Long? = null,
    val schoolEnd: Long? = null,
    val long: Double? = null,
    val lat: Double? = null,
    val rad: Double? = null,

    val attendanceStartError: String? = null,
    val attendanceEndError: String? = null,
    val schoolStartError: String? = null,
    val schoolEndError: String? = null,
) {
    val isValid
        get() = listOf(
            attendanceStartError,
            attendanceEndError,
            schoolStartError,
            schoolEndError
        ).all { it == null }
}
