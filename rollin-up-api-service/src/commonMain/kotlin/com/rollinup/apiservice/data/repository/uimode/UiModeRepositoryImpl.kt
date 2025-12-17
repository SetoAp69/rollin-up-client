package com.rollinup.apiservice.data.repository.uimode

import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import com.rollinup.common.model.UiMode

class UiModeRepositoryImpl(
    private val localDataStore: LocalDataStore,
) : UiModeRepository {
    override suspend fun getUiMode(): UiMode = localDataStore.getLocalUiModeSetting()

    override suspend fun updateUiMode(uiMode: UiMode) =
        localDataStore.updateLocalUiModeSetting(uiMode)
}