package com.rollinup.apiservice.data.repository.token

import com.rollinup.apiservice.data.source.datastore.LocalDataStore

class TokenRepositoryImpl(
    private val localDataStore: LocalDataStore,
) : TokenRepository {
    override suspend fun getToken(): String = localDataStore.getToken()

    override suspend fun clearToken() = localDataStore.clearToken()

    override suspend fun updateToken(token: String) = localDataStore.updateToken(token)

    override suspend fun getRefreshToken(): String = localDataStore.getRefreshToken()

    override suspend fun clearRefreshToken() = localDataStore.clearRefreshToken()

    override suspend fun updateRefreshToken(token: String) =
        localDataStore.updateRefreshToken(token)
}