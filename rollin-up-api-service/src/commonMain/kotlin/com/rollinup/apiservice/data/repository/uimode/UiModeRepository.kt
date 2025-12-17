package com.rollinup.apiservice.data.repository.uimode

import com.rollinup.common.model.UiMode

interface UiModeRepository {
    suspend fun getUiMode(): UiMode
    suspend fun updateUiMode(uiMode: UiMode)
}