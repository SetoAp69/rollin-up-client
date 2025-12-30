package com.rollinup.apiservice.data.repository.uimode

import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import com.rollinup.common.model.UiMode

/**
 * Implementation of the [UiModeRepository] interface.
 *
 * This repository manages the user's preference for the UI mode (Theme), persisting
 * it to local storage.
 *
 * @property localDataStore The local storage mechanism for persisting app settings.
 */
class UiModeRepositoryImpl(
    private val localDataStore: LocalDataStore,
) : UiModeRepository {
    /**
     * Retrieves the currently saved UI mode setting (e.g., Light, Dark, System).
     *
     * @return The stored [UiMode].
     */
    override suspend fun getUiMode(): UiMode = localDataStore.getLocalUiModeSetting()

    /**
     * Updates the saved UI mode setting.
     *
     * @param uiMode The new [UiMode] to persist.
     */
    override suspend fun updateUiMode(uiMode: UiMode) =
        localDataStore.updateLocalUiModeSetting(uiMode)
}