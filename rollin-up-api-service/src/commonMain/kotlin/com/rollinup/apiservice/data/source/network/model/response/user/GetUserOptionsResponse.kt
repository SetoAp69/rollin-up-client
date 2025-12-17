package com.rollinup.apiservice.data.source.network.model.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserOptionsResponse(
    @SerialName("status")
    val status: Int = 0,
    @SerialName("message")
    val message: String = "",
    @SerialName("data")
    val data: Data = Data(),
) {
    @Serializable
    data class Data(
        @SerialName("roles")
        val rolesOptions: List<Data<Int>> = emptyList(),
        @SerialName("class")
        val classOptions: List<Data<Int>> = emptyList(),
        @SerialName("rolesId")
        val rolesIdOptions: List<Data<String>> = emptyList(),
        @SerialName("classId")
        val classIdOptions: List<Data<String>> = emptyList(),
    ) {
        @Serializable
        data class Data<T>(
            @SerialName("label")
            val label: String = "",
            @SerialName("value")
            val value: T,
        )
    }
}
