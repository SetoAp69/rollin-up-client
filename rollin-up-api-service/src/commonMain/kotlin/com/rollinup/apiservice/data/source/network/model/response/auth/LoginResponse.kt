package com.rollinup.apiservice.data.source.network.model.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("status")
    val status: Int = 0,
    @SerialName("message")
    val message: String = "",
    @SerialName("data")
    val data: Data = Data(),
) {
    @Serializable
    data class Data(
        @SerialName("data")
        val data: UserData = UserData(),
        @SerialName("access_token")
        val accessToken: String = "",
        @SerialName("refresh_token")
        val refreshToken: String = "",
    ) {
        @Serializable
        data class UserData(
            @SerialName("id")
            val id: String = "",
            @SerialName("deviceId")
            val deviceId: String? = null,
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
            @SerialName("classId")
            val classId: String? = null,
            @SerialName("classKey")
            val classKey: Int? = null,
        )
    }

}
