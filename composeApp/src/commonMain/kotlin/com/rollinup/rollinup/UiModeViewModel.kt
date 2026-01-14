package com.rollinup.rollinup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.domain.uimode.GetUiModeUseCase
import com.rollinup.apiservice.domain.uimode.UpdateUiModeUseCase
import com.rollinup.common.model.UiMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UiModeViewModel(
    private val updateUiModeUseCase: UpdateUiModeUseCase,
    private val getUiModeUseCase: GetUiModeUseCase,
) : ViewModel() {
    private val _uiMode = MutableStateFlow(UiMode.AUTO)
    val uiMode = _uiMode.asStateFlow()

    fun getUiMode() {
        viewModelScope.launch {
            val value = getUiModeUseCase()
            _uiMode.update { value }
        }
    }

    fun updateUiMode(uiMode: UiMode) {
        viewModelScope.launch {
            updateUiModeUseCase(uiMode)
            getUiMode()
        }
    }

}