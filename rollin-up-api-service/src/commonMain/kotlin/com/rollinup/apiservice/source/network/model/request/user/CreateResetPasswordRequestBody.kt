package com.rollinup.apiservice.source.network.model.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateResetPasswordRequestBody(
    @SerialName("email")
    val email: String = "",
)
