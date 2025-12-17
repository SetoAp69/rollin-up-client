package com.rollinup.apiservice.data.source.network.model.response.globalsetting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetGlobalSettingResponse(
    @SerialName("status")
    val status: Int = 0,
    @SerialName("message")
    val message: String = "",
    @SerialName("data")
    val `data`: Data = Data()
) {
    @Serializable
    data class Data(
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
        @SerialName("modifiedBY")
        val modifiedBY: ModifiedBY = ModifiedBY()
    ) {
        @Serializable
        data class ModifiedBY(
            @SerialName("id")
            val id: String = "",
            @SerialName("name")
            val name: String = ""
        )
    }
}