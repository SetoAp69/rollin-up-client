package com.rollinup.apiservice.source.network.model.response.sse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeneralSettingResponse(
    @SerialName("semesterStart")
    val semesterStart: String = "",
    @SerialName("semesterEnd")
    val semesterEnd: String = "",
    @SerialName("updatedAt")
    val updatedAt: String = "",
    @SerialName("schoolPeriodStart")
    val schoolPeriodStart: String = "",
    @SerialName("schoolPeriodEnd")
    val schoolPeriodEnd: String = "",
    @SerialName("checkInPeriodStart")
    val checkInPeriodStart: String = "",
    @SerialName("checkInPeriodEnd")
    val checkInPeriodEnd: String = "",
    @SerialName("latitude")
    val latitude: Double = 0.0,
    @SerialName("longitude")
    val longitude: Double = 0.0,
    @SerialName("radius")
    val radius: Double = 0.0,
)
