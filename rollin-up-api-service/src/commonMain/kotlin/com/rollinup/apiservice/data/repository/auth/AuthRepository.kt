package com.rollinup.apiservice.data.repository.auth

import com.rollinup.apiservice.data.source.network.model.request.auth.LoginBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(body: LoginBody): Flow<Result<LoginEntity, NetworkError>>

    fun loginJWT(token: String): Flow<Result<LoginEntity, NetworkError>>

    fun updatePasswordAndDevice(
        id: String,
        body: UpdatePasswordAndVerificationBody,
        token: String,
    ): Flow<Result<Unit, NetworkError>>

    suspend fun clearClientToken()
}