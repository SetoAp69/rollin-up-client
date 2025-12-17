package com.rollinup.apiservice.data.source.network.model.response.attendance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetDashboardDataResponse(
    @SerialName("status")
    val status: Int = 0,
    @SerialName("message")
    val message: String = "",
    @SerialName("data")
    val data: Data = Data(),
) {
    @Serializable
    data class Data(
        @SerialName("currentStatus")
        val status: String = "",
        @SerialName("summary")
        val summary: Summary = Summary(),
    ) {
        @Serializable
        data class Summary(
            @SerialName("checkedIn")
            val checkedIn: Long = 0L,
            @SerialName("late")
            val late: Long = 0L,
            @SerialName("excused")
            val excused: Long = 0L,
            @SerialName("approvalPending")
            val approvalPending: Long = 0L,
            @SerialName("absent")
            val absent: Long = 0L,
            @SerialName("sick")
            val sick: Long = 0L,
            @SerialName("other")
            val other: Long = 0L,
        )
    }
}
