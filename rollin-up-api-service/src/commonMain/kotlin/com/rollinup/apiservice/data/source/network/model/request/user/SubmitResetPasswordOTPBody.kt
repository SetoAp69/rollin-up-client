package com.rollinup.apiservice.data.source.network.model.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitResetPasswordOTPBody(
    @SerialName("email")
    val email: String = "",
    @SerialName("otp")
    val otp: String = "",
)
