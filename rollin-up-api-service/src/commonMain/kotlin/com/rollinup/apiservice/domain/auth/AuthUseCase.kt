package com.rollinup.apiservice.domain.auth

import com.rollinup.apiservice.data.repository.auth.AuthRepository
import com.rollinup.apiservice.data.source.network.model.request.auth.LoginBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.NetworkError
import com.rollinup.apiservice.model.common.Result
import kotlinx.coroutines.flow.Flow


class LoginJWTUseCase(private val repository: AuthRepository) {
    operator fun invoke(token: String): Flow<Result<LoginEntity, NetworkError>> {
        return repository.loginJWT(token)
    }
}

class LoginUseCase(private val repository: AuthRepository) {
    operator fun invoke(body: LoginBody): Flow<Result<LoginEntity, NetworkError>> =
        repository.login(body)
}

class UpdatePasswordAndDeviceUseCase(private val repository: AuthRepository) {
    operator fun invoke(id: String, body: UpdatePasswordAndVerificationBody, token: String) =
        repository.updatePasswordAndDevice(id, body, token)
}

class ClearClientTokenUseCase(private val repository: AuthRepository){
    suspend operator fun invoke() = repository.clearClientToken()
}