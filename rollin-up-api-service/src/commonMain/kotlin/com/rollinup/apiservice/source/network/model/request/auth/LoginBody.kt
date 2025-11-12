package com.rollinup.apiservice.source.network.model.request.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginBody(
    @SerialName("username")
    val email: String = "",
    @SerialName("password")
    val password: String = ""
)
