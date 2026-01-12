package com.rollinup.apiservice.data.source.network.model.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitResetPasswordBody(
    @SerialName("token")
    val token: String = "",
    @SerialName("newPassword")
    val newPassword: String = "",
)