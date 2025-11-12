package com.rollinup.apiservice.data.source.network.model.response.attendance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAttendanceListByStudentResponse(
    @SerialName("status")
    val status: Int = 0,
    @SerialName("message")
    val message: String = "",
    @SerialName("data")
    val data: Data = Data(),
) {
    @Serializable
    data class Data(
        @SerialName("record")
        val record: Int = 0,
        @SerialName("page")
        val page: Int = 0,
        @SerialName("summary")
        val summary: Summary = Summary(),
        @SerialName("data")
        val data: List<GetAttendanceByStudentListDTO> = emptyList(),
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

        @Serializable
        data class GetAttendanceByStudentListDTO(
            @SerialName("id")
            val id: String = "",
            @SerialName("status")
            val status: String = "",
            @SerialName("checkIn")
            val checkedInAt: String? = null,
            @SerialName("permit")
            val permit: Permit? = null,
            @SerialName("createdAt")
            val createdAt: String = "",
            @SerialName("updatedAt")
            val updatedAt: String = "",
            @SerialName("date")
            val date: String = "",
        )

        @Serializable
        data class Permit(
            @SerialName("id")
            val id: String = "",
            @SerialName("reason")
            val reason: String? = null,
            @SerialName("type")
            val type: String = "",
            @SerialName("startTime")
            val startTime: String = "",
            @SerialName("endTime")
            val endTime: String = "",
        )
    }
}
