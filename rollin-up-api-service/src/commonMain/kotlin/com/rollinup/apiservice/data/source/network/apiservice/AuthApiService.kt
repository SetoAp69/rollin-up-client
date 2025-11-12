package com.rollinup.apiservice.data.source.network.apiservice

import com.rollinup.apiservice.data.source.network.model.request.auth.LoginBody
import com.rollinup.apiservice.data.source.network.model.response.ApiResponse
import com.rollinup.apiservice.data.source.network.model.response.auth.LoginResponse

interface AuthApiService {
    suspend fun loginJWT(token: String): ApiResponse<LoginResponse>

    suspend fun login(body: LoginBody): ApiResponse<LoginResponse>
}