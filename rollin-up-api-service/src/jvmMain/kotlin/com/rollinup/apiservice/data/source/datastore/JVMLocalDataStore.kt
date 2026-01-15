package com.rollinup.apiservice.data.source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rollinup.apiservice.Constant
import com.rollinup.apiservice.model.common.GlobalSetting
import com.rollinup.common.model.UiMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import okio.Path.Companion.toPath
import java.io.File

class JVMLocalDataStore : LocalDataStore {
    val userHome = System.getProperty("user.home") as String
    val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath {
        val file =
            File(userHome + "/" + Constant.LOCAL_DATA_STORE_PATH + Constant.LOCAL_DATA_STORE_NAME).apply { parentFile?.mkdirs() }

        if (!file.exists()) {
            file.createNewFile()
        }

        file.absolutePath.toPath()
    }

    override suspend fun getToken(): String {
        return dataStore.data.map { preferences -> preferences[stringPreferencesKey(Constant.ACCESS_TOKEN_KEY)] }
            .first() ?: ""
    }

    override suspend fun clearToken() {
        dataStore.edit { prefs ->
            val key = stringPreferencesKey(Constant.ACCESS_TOKEN_KEY)
            prefs.remove(key)
        }
    }

    override suspend fun updateToken(token: String) {
        dataStore.edit { prefs ->
            val key = stringPreferencesKey(Constant.ACCESS_TOKEN_KEY)
            prefs[key] = token
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

    override suspend fun getLocalGlobalSetting(): GlobalSetting? {
        val key = stringPreferencesKey(Constant.GLOBAL_SETTING_KEY)
        val setting = dataStore.data.map { datastore -> datastore[key] }

        return setting.firstOrNull()?.let { data ->
            Json.decodeFromString<GlobalSetting>(data)
        }
    }

    override suspend fun updateGlobalSetting(globalSetting: GlobalSetting) {
        val key = stringPreferencesKey(Constant.GLOBAL_SETTING_KEY)
        dataStore.edit { prefs ->
            prefs[key] = Json.encodeToString(globalSetting)
        }
    }

    override suspend fun clearGlobalSetting() {
        val key = stringPreferencesKey(Constant.GLOBAL_SETTING_KEY)
        dataStore.edit { prefs ->
            prefs.remove(key)
        }
    }

    override suspend fun getLocalUiModeSetting(): UiMode {
        val key = stringPreferencesKey(Constant.UI_MODE_KEY)
        val uiMode = dataStore.data.map { dataStore -> dataStore[key] }.first()

        return UiMode.entries.find { it.name.equals(uiMode, true) } ?: UiMode.AUTO
    }

    override suspend fun updateLocalUiModeSetting(uiMode: UiMode) {
        val key = stringPreferencesKey(Constant.UI_MODE_KEY)
        dataStore.edit { prefs ->
            prefs[key] = uiMode.name
        }
    }
}