package com.rollinup.apiservice.data.source.network.apiservice

import com.rollinup.apiservice.data.source.network.model.request.auth.LoginBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.auth.LoginResponse

interface AuthApiService {
    suspend fun loginJWT(token: String): ApiResponse<LoginResponse>

    suspend fun login(body: LoginBody): ApiResponse<LoginResponse>

    suspend fun updatePasswordAndDevice(
        id: String,
        token: String,
        body: UpdatePasswordAndVerificationBody,
    ): ApiResponse<Unit>

    suspend fun logout()
}