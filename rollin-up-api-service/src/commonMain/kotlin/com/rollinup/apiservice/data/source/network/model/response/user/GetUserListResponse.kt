package com.rollinup.apiservice.data.source.network.model.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GetUserListResponse(
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
        @SerialName("data")
        val data: List<UserData> = emptyList(),
    ) {
        @Serializable
        data class UserData(
            @SerialName("id")
            val id: String = "",
            @SerialName("user_name")
            val userName: String = "",
            @SerialName("email")
            val email: String = "",
            @SerialName("first_name")
            val firstName: String = "",
            @SerialName("last_name")
            val lastName: String = "",
            @SerialName("role")
            val role: String = "",
            @SerialName("gender")
            val gender: String = "",
            @SerialName("class")
            val classX: String? = null,
            @SerialName("studentId")
            val studentId: String? = null,
            @SerialName("address")
            val address: String = "",
        )
    }
}