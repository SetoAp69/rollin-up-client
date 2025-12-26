package com.rollinup.apiservice.data.repository.token

interface TokenRepository {
    suspend fun getToken(): String
    suspend fun clearToken()
    suspend fun updateToken(token: String): Unit

    suspend fun getRefreshToken(): String
    suspend fun clearRefreshToken()
    suspend fun updateRefreshToken(token: String): Unit
}