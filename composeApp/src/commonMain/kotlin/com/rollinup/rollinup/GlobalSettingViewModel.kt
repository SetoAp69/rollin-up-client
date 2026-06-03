package com.rollinup.rollinup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.domain.globalsetting.GetCachedGlobalSettingUseCase
import com.rollinup.apiservice.domain.globalsetting.GetGlobalSettingUseCase
import com.rollinup.apiservice.domain.globalsetting.ListenGlobalSettingSSE
import com.rollinup.apiservice.domain.globalsetting.UpdateCachedGlobalSettingUseCase
import com.rollinup.apiservice.model.common.GlobalSetting
import com.rollinup.apiservice.model.common.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GlobalSettingViewModel(
    private val listenGlobalSettingSSE: ListenGlobalSettingSSE,
    private val getCachedGlobalSettingUseCase: GetCachedGlobalSettingUseCase,
    private val updateCachedGlobalSettingUseCase: UpdateCachedGlobalSettingUseCase,
    private val getGlobalSettingUseCase: GetGlobalSettingUseCase,
) : ViewModel() {

    private var _globalSetting = MutableStateFlow(GlobalSetting())
    val globalSetting = _globalSetting.asStateFlow()

    private var _initState = MutableStateFlow<Boolean?>(null)
    val initState = _initState.asStateFlow()

    var sseJob : Job? = null

    fun init() {
        viewModelScope.launch {
            getGlobalSettingUseCase().collect { result ->
                _initState.value = result is Result.Success
                if (result is Result.Success) {
                    updateCachedGlobalSettingUseCase(result.data)
                    _globalSetting.update { result.data }
                }
            }
        }
    }

    fun fetchLocalSetting() {
        viewModelScope.launch {
            getCachedGlobalSettingUseCase()?.let {
                _globalSetting.update { it }
            }
        }
    }

    fun listen() {
        sseJob?.cancel()
        sseJob = viewModelScope.launch {
            // Fallback polling loop: ensures we get updates even if SSE silently hangs (common on mobile)
            launch {
                while (true) {
                    kotlinx.coroutines.delay(10000L)
                    try {
                        getGlobalSettingUseCase().collect { result ->
                            if (result is Result.Success) {
                                _globalSetting.value = result.data
                                updateCachedGlobalSetting(result.data)
                            }
                        }
                    } catch (e: Exception) {
                        // Ignore polling errors
                    }
                }
            }

            // Primary SSE loop
            while (true) {
                try {
                    listenGlobalSettingSSE().collect {
                        getGlobalSettingUseCase().collect { result ->
                            if (result is Result.Success) {
                                _globalSetting.value = result.data
                                updateCachedGlobalSetting(result.data)
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Ignore error and let it retry
                }
                kotlinx.coroutines.delay(5000L) // Wait 5 seconds before attempting to reconnect
            }
        }
    }

    fun stopListening() {
        sseJob?.cancel()
        sseJob = null
    }

    private suspend fun updateCachedGlobalSetting(globalSetting: GlobalSetting) {
        updateCachedGlobalSettingUseCase(globalSetting)
    }

}
