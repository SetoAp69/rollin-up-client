package com.rollinup.apiservice.data.source.datastore

import com.rollinup.apiservice.model.common.GlobalSetting
import com.rollinup.common.model.LocaleEnum
import com.rollinup.common.model.UiMode

interface LocalDataStore {
    suspend fun getToken(): String
    suspend fun clearToken()
    suspend fun updateToken(token: String)

    suspend fun getRefreshToken(): String
    suspend fun clearRefreshToken()
    suspend fun updateRefreshToken(token: String)

    suspend fun getLocalGlobalSetting(): GlobalSetting?
    suspend fun updateGlobalSetting(globalSetting: GlobalSetting)
    suspend fun clearGlobalSetting()

    suspend fun getLocalUiModeSetting(): UiMode
    suspend fun updateLocalUiModeSetting(uiMode: UiMode)

    suspend fun getLocale(): LocaleEnum
    suspend fun setLocale(locale: LocaleEnum)
}

