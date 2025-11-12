package com.rollinup.apiservice.mapper

import com.rollinup.apiservice.model.Gender
import com.rollinup.apiservice.model.Role
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.source.network.model.response.auth.LoginResponse

class LoginMapper {
    fun mapLoginResponse(data: LoginResponse.Data): LoginEntity {
        return LoginEntity(
            id = data.data.id,
            deviceId = data.data.deviceId,
            userName = data.data.userName,
            email = data.data.email,
            firstName = data.data.firstName,
            lastName = data.data.lastName,
            role = Role.fromValue(data.data.role),
            gender = Gender.fromValue(data.data.gender),
            refreshToken = data.refreshToken,
            accessToken = data.accessToken
        )
    }
}