package com.rollinup.apiservice.domain.uimode

import com.rollinup.apiservice.data.repository.uimode.UiModeRepository
import com.rollinup.common.model.UiMode

class GetUiModeUseCase(private val repository: UiModeRepository) {
    suspend operator fun invoke() = repository.getUiMode()
}

class UpdateUiModeUseCase(private val repository: UiModeRepository) {
    suspend operator fun invoke(uiMode: UiMode) = repository.updateUiMode(uiMode)
}