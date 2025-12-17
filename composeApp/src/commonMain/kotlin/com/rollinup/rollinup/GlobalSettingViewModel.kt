package com.rollinup.rollinup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.domain.globalsetting.GetCachedGlobalSettingUseCase
import com.rollinup.apiservice.domain.globalsetting.GetGlobalSettingUseCase
import com.rollinup.apiservice.domain.globalsetting.ListenGlobalSettingSSE
import com.rollinup.apiservice.model.common.GlobalSetting
import com.rollinup.apiservice.model.common.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GlobalSettingViewModel(
    private val listenGlobalSettingSSE: ListenGlobalSettingSSE,
    private val getCachedGlobalSettingUseCase: GetCachedGlobalSettingUseCase,
    private val getGlobalSettingUseCase: GetGlobalSettingUseCase,
) : ViewModel() {

    private var _globalSetting = MutableStateFlow(GlobalSetting())
    val globalSetting = _globalSetting.asStateFlow()

    fun init(){
        viewModelScope.launch {
            getGlobalSettingUseCase().collect { result ->
                if(result is Result.Success){
                    _globalSetting.value = result.data
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
        viewModelScope.launch {
            listenGlobalSettingSSE().collect {
                if (it is Result.Success) {
                    fetchLocalSetting()
                }
            }
        }
    }
}