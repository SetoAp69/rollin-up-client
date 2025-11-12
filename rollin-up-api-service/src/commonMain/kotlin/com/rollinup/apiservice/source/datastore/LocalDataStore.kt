package com.rollinup.apiservice.source.datastore

import com.rollinup.apiservice.model.GeneralSetting
import kotlinx.coroutines.flow.Flow

interface LocalDataStore {
    suspend fun getToken(): String
    suspend fun clearToken()
    suspend fun updateToken(token: String)

    suspend fun getRefreshToken(): String
    suspend fun clearRefreshToken()
    suspend fun updateRefreshToken(token: String)

    suspend fun getLocalGeneralSetting(): Flow<GeneralSetting?>
    suspend fun updateGeneralSetting(generalSetting: GeneralSetting)
    suspend fun clearGeneralSetting()

}

