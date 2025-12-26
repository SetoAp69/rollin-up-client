package com.rollinup.apiservice.data.source.network.model.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserByIdResponse(
    @SerialName("status")
    val status: Int = 0,
    @SerialName("message")
    val message: String = "",
    @SerialName("data")
    val data: Data = Data(),
) {
    @Serializable
    data class Data(
        @SerialName("id")
        val id: String = "",
        @SerialName("username")
        val username: String = "",
        @SerialName("firstName")
        val firstName: String = "",
        @SerialName("lastName")
        val lastName: String = "",
        @SerialName("studentId")
        val studentId: String? = null,
        @SerialName("phoneNumber")
        val phoneNumber: String? = null,
        @SerialName("role")
        val role: Role = Role(),
        @SerialName("class")
        val classX: Class? = null,
        @SerialName("address")
        val address: String = "",
        @SerialName("gender")
        val gender: String = "",
        @SerialName("email")
        val email: String = "",
        @SerialName("birthday")
        val birthday: String = "",
    ) {
        @Serializable
        data class Role(
            @SerialName("id")
            val id: String = "",
            @SerialName("name")
            val name: String = "",
            @SerialName("key")
            val key: Int = 0,
        )

        @Serializable
        data class Class(
            @SerialName("id")
            val id: String = "",
            @SerialName("key")
            val key: Int = 0,
            @SerialName("name")
            val name: String = "",
            @SerialName("grade")
            val grade: Int = 0,
        )
    }
}
