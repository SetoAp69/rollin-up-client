package com.rollinup.apiservice.model.common

import com.rollinup.common.utils.Utils.toLocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GlobalSetting(
    @SerialName("semester_start")
    val semesterStart: String = "",

    @SerialName("semester_end")
    val semesterEnd: String = "",

    @SerialName("updated_at")
    val updatedAt: String = "",

    @SerialName("school_period_start")
    private val _schoolPeriodStart: String = "07:00",

    @SerialName("school_period_end")
    private val _schoolPeriodEnd: String = "15:00",

    @SerialName("checkin_period_start")
    private val _checkInPeriodStart: String = "",

    @SerialName("checkin_period_end")
    private val _checkInPeriodEnd: String = "",

    @SerialName("latitude")
    val latitude: Double = 0.0,

    @SerialName("longitude")
    val longitude: Double = 0.0,

    @SerialName("radius")
    val radius: Double = 0.0,
) {
    val schoolPeriodStart
        get() = _schoolPeriodStart.toLocalTime()

    val schoolPeriodEnd
        get() = _schoolPeriodEnd.toLocalTime()

    val checkInPeriodStart
        get() = _checkInPeriodEnd.toLocalTime()

    val checkInPeriodEnd
        get() = _checkInPeriodEnd.toLocalTime()
}
