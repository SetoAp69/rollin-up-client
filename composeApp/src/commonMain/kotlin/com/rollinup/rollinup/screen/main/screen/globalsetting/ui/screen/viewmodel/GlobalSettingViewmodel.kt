package com.rollinup.rollinup.screen.main.screen.globalsetting.ui.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.data.source.network.model.request.globalsetting.EditGlobalSettingBody
import com.rollinup.apiservice.domain.globalsetting.EditGlobalSettingUseCase
import com.rollinup.apiservice.domain.globalsetting.GetGlobalSettingUseCase
import com.rollinup.apiservice.model.common.GlobalSetting
import com.rollinup.apiservice.model.common.Result
import com.rollinup.common.utils.Utils.fromUTCTime
import com.rollinup.common.utils.Utils.toLocalTime
import com.rollinup.common.utils.Utils.toUTCTime
import com.rollinup.rollinup.screen.main.screen.globalsetting.model.GlobalSettingCallback
import com.rollinup.rollinup.screen.main.screen.globalsetting.model.GlobalSettingFormData
import com.rollinup.rollinup.screen.main.screen.globalsetting.ui.screen.uistate.GlobalSettingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GlobalSettingViewmodel(
    private val getGlobalSettingUseCase: GetGlobalSettingUseCase,
    private val editGlobalSettingUseCase: EditGlobalSettingUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GlobalSettingUiState())
    val uiState = _uiState.asStateFlow()

    fun init() {
        getGlobalSetting()
    }

    fun getCallback() =
        GlobalSettingCallback(
            onRefresh = ::getGlobalSetting,
            onUpdateForm = ::updateForm,
            onSubmit = ::submitForm,
            onResetMessageState = ::resetMessageState
        )

    private fun getGlobalSetting() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getGlobalSettingUseCase().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        val form = fetchFormFromEntity(result.data)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                formData = form
                            )
                        }
                    }

                    else -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    private fun updateForm(formData: GlobalSettingFormData) {
        _uiState.update { it.copy(formData = formData) }
    }

    private fun resetMessageState() {
        _uiState.update { it.copy(submitState = null) }
    }

    private fun submitForm(formData: GlobalSettingFormData) {
        if (!validateForm(formData)) return
        val body = mapBodyFromForm(formData)
        viewModelScope.launch {
            editGlobalSettingUseCase(body).collectLatest { result ->
                _uiState.update {
                    it.copy(
                        submitState = result is Result.Success
                    )
                }
            }
        }
    }

    private fun fetchFormFromEntity(entity: GlobalSetting) =
        GlobalSettingFormData(
            attendanceStart = entity.checkInPeriodStart.toSecondOfDay().toLong(),
            attendanceEnd = entity.checkInPeriodEnd.toSecondOfDay().toLong(),
            schoolStart = entity.schoolPeriodStart.toSecondOfDay().toLong(),
            schoolEnd = entity.schoolPeriodEnd.toSecondOfDay().toLong(),
            long = entity.longitude,
            lat = entity.latitude,
            rad = entity.radius,
        )

    private fun mapBodyFromForm(formData: GlobalSettingFormData): EditGlobalSettingBody {
        return EditGlobalSettingBody(
            checkInPeriodStart = formData.attendanceStart,
            checkInPeriodEnd = formData.attendanceEnd,
            schoolPeriodStart = formData.schoolStart,
            schoolPeriodEnd = formData.schoolEnd,
            longitude = formData.long,
            latitude = formData.lat,
            radius = formData.rad,
        )
    }

    private fun validateForm(formData: GlobalSettingFormData): Boolean {
        var formData = formData
        if ((formData.attendanceStart ?: 0) > (formData.attendanceEnd ?: 0)) {
            val errorMsg = "The start period cannot be later than the end period."
            formData = formData.copy(
                attendanceStartError = errorMsg,
                attendanceEndError = errorMsg
            )
        }
        if ((formData.schoolStart ?: 0) > (formData.schoolEnd ?: 0)) {
            val errorMsg = "The start period cannot be later than the end period."
            formData = formData.copy(
                schoolEndError = errorMsg,
                schoolStartError = errorMsg
            )
        }

        _uiState.update {
            it.copy(formData = formData)
        }

        return formData.isValid
    }
}