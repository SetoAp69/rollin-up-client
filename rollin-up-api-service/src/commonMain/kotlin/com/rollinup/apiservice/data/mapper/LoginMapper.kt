package com.rollinup.apiservice.data.mapper

import com.rollinup.apiservice.model.common.Gender
import com.rollinup.apiservice.model.common.Role
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.data.source.network.model.response.auth.LoginResponse

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
            accessToken = data.accessToken,
            classX = data.data.classX,
            classId = data.data.classId,
            classKey = data.data.classKey,
        )
    }
}