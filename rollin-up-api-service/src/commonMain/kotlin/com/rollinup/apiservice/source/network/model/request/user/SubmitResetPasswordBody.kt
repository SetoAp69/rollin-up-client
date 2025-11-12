package com.rollinup.apiservice.source.network.model.request.user

import kotlinx.serialization.SerialName

data class SubmitResetPasswordBody(
    @SerialName("token")
    val token: String = "",
    @SerialName("newPasword")
    val newPassword: String = "",
)