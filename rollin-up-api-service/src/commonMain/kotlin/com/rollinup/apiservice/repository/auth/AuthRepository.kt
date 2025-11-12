package com.rollinup.apiservice.repository.auth

import com.rollinup.apiservice.model.NetworkError
import com.rollinup.apiservice.model.Result
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.source.network.model.request.auth.LoginBody
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(body: LoginBody): Flow<Result<LoginEntity, NetworkError>>

    fun loginJWT(token:String):Flow<Result<LoginEntity, NetworkError>>
}