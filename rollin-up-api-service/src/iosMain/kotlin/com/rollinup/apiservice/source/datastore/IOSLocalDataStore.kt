@file:OptIn(ExperimentalForeignApi::class)

package com.rollinup.apiservice.source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rollinup.apiservice.Constant
import com.rollinup.apiservice.model.common.GlobalSetting
import com.rollinup.apiservice.source.datastore.LocalDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

class IOSLocalDataStore : LocalDataStore {
    val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath {
        val dir = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        (requireNotNull(dir).path() + Constant.LOCAL_DATA_STORE_NAME).toPath()
    }

    override suspend fun getToken(): String {
        val key = stringPreferencesKey(Constant.ACCESS_TOKEN_KEY)
        return dataStore.data.map { preferences -> preferences[key] }.first() ?: ""
    }

    override suspend fun clearToken() {
        val key = stringPreferencesKey(Constant.ACCESS_TOKEN_KEY)
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    override suspend fun updateToken(token: String) {
        val key = stringPreferencesKey(Constant.ACCESS_TOKEN_KEY)
        dataStore.edit { preferences ->
            preferences[key] = token
        }
    }

    override suspend fun getRefreshToken(): String {
        val key = stringPreferencesKey(Constant.REFRESH_TOKEN_KEY)
        return dataStore.data.map { datastore -> datastore[key] }
            .first() ?: ""
    }

    override suspend fun clearRefreshToken() {
        val key = stringPreferencesKey(Constant.REFRESH_TOKEN_KEY)
        dataStore.edit { prefs ->
            prefs.remove(key)
        }
    }

    override suspend fun updateRefreshToken(token: String) {
        val key = stringPreferencesKey(Constant.REFRESH_TOKEN_KEY)
        dataStore.edit { prefs ->
            prefs[key] = token
        }
    }

    override suspend fun getLocalGeneralSetting(): Flow<GlobalSetting?> {
        val key = stringPreferencesKey(Constant.GLOBAL_SETTING_KEY)
        val setting = dataStore.data.map { datastore -> datastore[key] }

        return setting.let { flow ->
            flow.map { value ->
                value?.let {
                    Json.decodeFromString<GlobalSetting>(it)
                }
            }
        }
    }

    override suspend fun updateGeneralSetting(generalSetting: GlobalSetting) {
        val key = stringPreferencesKey(Constant.GLOBAL_SETTING_KEY)
        dataStore.edit { prefs ->
            prefs[key] = Json.encodeToString(generalSetting)
        }
    }

    override suspend fun clearGeneralSetting() {
        val key = stringPreferencesKey(Constant.GLOBAL_SETTING_KEY)
        dataStore.edit { prefs ->
            prefs.remove(key)
        }
    }

}