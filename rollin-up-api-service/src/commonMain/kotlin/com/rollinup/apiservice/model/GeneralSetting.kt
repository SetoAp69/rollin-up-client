package com.rollinup.apiservice.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeneralSetting(
    @SerialName("semester_start")
    val semesterStart: String = "",

    @SerialName("semester_end")
    val semesterEnd: String = "",

    @SerialName("updated_at")
    val updatedAt: String = "",

    @SerialName("school_period_start")
    val schoolPeriodStart: String = "",

    @SerialName("school_period_end")
    val schoolPeriodEnd: String = "",

    @SerialName("checkin_period_start")
    val checkInPeriodStart: String = "",

    @SerialName("checkin_period_end")
    val checkInPeriodEnd: String = "",

    @SerialName("latitude")
    val latitude: Double = 0.0,

    @SerialName("longitude")
    val longitude: Double = 0.0,

    @SerialName("radius")
    val radius: Double = 0.0,
)
