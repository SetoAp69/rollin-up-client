package com.rollinup.rollinup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import com.rollinup.apiservice.domain.generalsetting.ListenGeneralSettingSSE
import com.rollinup.apiservice.model.common.GeneralSetting
import com.rollinup.apiservice.model.common.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GeneralSettingViewModel(
    private val listenGeneralSettingSSE: ListenGeneralSettingSSE,
    private val localDataStore: LocalDataStore,
) : ViewModel() {

    private var _generalSetting = MutableStateFlow(GeneralSetting())
    val generalSetting = _generalSetting.asStateFlow()

    fun fetchLocalSetting() {
        viewModelScope.launch {
            localDataStore.getLocalGeneralSetting().collect { setting ->
                setting?.let {
                    _generalSetting.value = it
                }
            }
        }
    }

    fun listen() {
        L.wtf { "GeneralSettingViewmodel.listen()" }

        viewModelScope.launch {
            listenGeneralSettingSSE().collect {
                L.wtf { "collecting generalsetting sse" }
                if (it is Result.Success) {
                    localDataStore.updateGeneralSetting(it.data)
                    localDataStore.getLocalGeneralSetting().first()?.let {
                        _generalSetting.value = it
                    }
                } else {
                    L.wtf { "error in generalsetting sse" }
                }
            }
        }
    }
}