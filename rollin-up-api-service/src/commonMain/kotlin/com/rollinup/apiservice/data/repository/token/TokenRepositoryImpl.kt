package com.rollinup.apiservice.data.repository.token

import com.rollinup.apiservice.data.source.datastore.LocalDataStore

/**
 * Implementation of the [TokenRepository] interface.
 *
 * This repository is responsible for the persistence and retrieval of authentication tokens
 * (Access Token and Refresh Token) using a local data store.
 *
 * @property localDataStore The local storage mechanism (e.g., DataStore, SharedPreferences).
 */
class TokenRepositoryImpl(
    private val localDataStore: LocalDataStore,
) : TokenRepository {
    /**
     * Retrieves the currently stored access token.
     */
    override suspend fun getToken(): String = localDataStore.getToken()

    /**
     * Removes the stored access token.
     */
    override suspend fun clearToken() = localDataStore.clearToken()

    /**
     * Updates the stored access token with a new value.
     *
     * @param token The new access token to store.
     */
    override suspend fun updateToken(token: String) = localDataStore.updateToken(token)

    /**
     * Retrieves the currently stored refresh token.
     */
    override suspend fun getRefreshToken(): String = localDataStore.getRefreshToken()

    /**
     * Removes the stored refresh token.
     */
    override suspend fun clearRefreshToken() = localDataStore.clearRefreshToken()

    /**
     * Updates the stored refresh token with a new value.
     *
     * @param token The new refresh token to store.
     */
    override suspend fun updateRefreshToken(token: String) =
        localDataStore.updateRefreshToken(token)
}