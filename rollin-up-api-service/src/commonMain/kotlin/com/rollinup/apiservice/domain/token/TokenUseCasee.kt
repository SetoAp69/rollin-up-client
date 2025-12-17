package com.rollinup.apiservice.domain.token

import com.rollinup.apiservice.data.repository.token.TokenRepository

class GetTokenUseCase(private val repository: TokenRepository) {
    suspend operator fun invoke() = repository.getToken()
}

class UpdateTokenUseCase(private val repository: TokenRepository) {
    suspend operator fun invoke(token: String) = repository.updateToken(token)
}

class ClearTokenUseCase(private val repository: TokenRepository) {
    suspend operator fun invoke() = repository.clearToken()
}

class GetRefreshTokenUseCase(private val repository: TokenRepository) {
    suspend operator fun invoke() = repository.getToken()
}

class UpdateRefreshTokenUseCase(private val repository: TokenRepository) {
    suspend operator fun invoke(token: String) = repository.updateToken(token)
}

class ClearRefreshTokenUseCase(private val repository: TokenRepository) {
    suspend operator fun invoke() = repository.clearToken()
}