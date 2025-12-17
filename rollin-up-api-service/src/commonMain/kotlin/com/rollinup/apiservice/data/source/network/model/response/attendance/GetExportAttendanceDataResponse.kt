package com.rollinup.apiservice.data.source.network.model.response.attendance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetExportAttendanceDataResponse(
    @SerialName("status")
    val status: Int = 0,
    @SerialName("message")
    val message: String = "",
    @SerialName("data")
    val data: Data = Data(),
) {
    @Serializable
    data class Data(
        @SerialName("dateRange")
        val sDateRange: List<String> = emptyList(),
        @SerialName("data")
        val data: List<Data> = emptyList(),
    ) {
        @Serializable
        data class Data(
            @SerialName("studentId")
            val studentId: String = "",
            @SerialName("fullName")
            val fullName: String = "",
            @SerialName("class")
            val classX: String = "",
            @SerialName("data")
            val dataPerDate: List<Data> = emptyList(),
        ) {
            @Serializable
            data class Data(
                @SerialName("date")
                val date: String = "",
                @SerialName("status")
                val status: String = "",
            )
        }
    }
}
