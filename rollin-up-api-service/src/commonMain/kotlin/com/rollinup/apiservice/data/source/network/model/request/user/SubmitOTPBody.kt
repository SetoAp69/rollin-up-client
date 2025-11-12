package com.rollinup.apiservice.data.source.network.model.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitOTPBody(
    @SerialName("email")
    val email: String = "",
    @SerialName("otp")
    val otp: String = "",
)
