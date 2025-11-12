package com.rollinup.apiservice.model.auth

import com.rollinup.apiservice.model.common.Gender
import com.rollinup.apiservice.model.common.Role

data class LoginEntity(
    val id: String = "",
    val deviceId: String? = null,
    val userName: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val role: Role = Role.UNKNOWN,
    val gender: Gender = Gender.MALE,
    val accessToken:String = "",
    val refreshToken:String = ""
)