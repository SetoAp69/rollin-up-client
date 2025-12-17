package com.rollinup.apiservice.data.source.network.model.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordAndVerificationBody(
    @SerialName("password")
    val password: String = "",
    @SerialName("deviceId")
    val deviceId: String? = null,
    @SerialName("token")
    val token:String = ""
)