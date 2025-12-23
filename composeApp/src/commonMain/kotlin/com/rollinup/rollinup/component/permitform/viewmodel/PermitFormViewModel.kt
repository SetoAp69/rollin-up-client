package com.rollinup.rollinup.component.permitform.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.data.source.network.model.request.permit.CreateEditPermitBody
import com.rollinup.apiservice.domain.permit.CreatePermitUseCase
import com.rollinup.apiservice.domain.permit.EditPermitUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByIdUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.model.permit.PermitType
import com.rollinup.common.utils.Utils.toEpochMillis
import com.rollinup.common.utils.Utils.parseToLocalDateTime
import com.rollinup.rollinup.component.permitform.model.PermitFormCallback
import com.rollinup.rollinup.component.permitform.model.PermitFormData
import com.rollinup.rollinup.component.permitform.uistate.PermitFormUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PermitFormViewModel(
    private val getPermitByIdUseCase: GetPermitByIdUseCase,
    private val editPermitUseCase: EditPermitUseCase,
    private val createPermitUseCase: CreatePermitUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PermitFormUiState())
    val uiState = _uiState.asStateFlow()

    fun init(
        id: String?,
        user: LoginEntity?
    ) {
        _uiState.update {
            it.copy(
                id = id,
                user = user
            )
        }

        if (id.isNullOrBlank()) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            getPermitByIdUseCase(id).collectLatest { result ->
                if (result is Result.Success) {
                    val formData = with(result.data) {
                        PermitFormData(
                            duration = listOf(
                                startTime.parseToLocalDateTime().toEpochMillis(),
                                endTime.parseToLocalDateTime().toEpochMillis()
                            ),
                            reason = reason,
                            type = type,
                            note = note,
                        )
                    }

                    _uiState.update { it.copy(formData = formData) }
                }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun reset() {
        _uiState.update { PermitFormUiState() }
    }

    fun getCallback() =
        PermitFormCallback(
            onSubmit = ::submit,
            onValidateForm = ::validateFormData,
            onUpdateFormData = ::updateFormData
        )

    private fun validateFormData(
        formData: PermitFormData,
    ): Boolean {
        var formData = formData

        if ((formData.duration.isEmpty() || formData.duration.any { it == null }) && !_uiState.value.isEdit)
            formData = formData.copy(durationError = "Duration can't be empty")

        if (formData.reason.isNullOrBlank() && formData.type == PermitType.ABSENCE && !_uiState.value.isEdit)
            formData = formData.copy(reasonError = "Please select a reason")

        if (formData.attachment ==null && !_uiState.value.isEdit)
            formData = formData.copy(reasonError = "Please put an attachment")

        _uiState.update { it.copy(formData = formData) }

        return formData.isValid
    }

    private fun updateFormData(
        formData: PermitFormData,
    ) {
        _uiState.update { it.copy(formData = formData) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun submit(
        formData: PermitFormData,
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) {
        if (!validateFormData(formData)) return

        val isEdit = _uiState.value.isEdit
        val userId = _uiState.value.user?.id ?: ""
        val body = with(formData) {
            CreateEditPermitBody(
                studentId = if (!isEdit) userId else null,
                reason = reason,
                duration = duration.filter { it != null }.ifEmpty { null } as List<Long>?,
                type = type,
                note = note,
                attachment = attachment,
            )
        }

        val domain =
            if (isEdit) editPermitUseCase(_uiState.value.id!!, body) else createPermitUseCase(body)

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingOverlay = true) }

            domain.collectLatest { result ->
                val isSuccess = result is Result.Success
                _uiState.update {
                    it.copy(
                        isLoadingOverlay = false,
                    )
                }
                if (isSuccess) {
                    onSuccess()
                } else {
                    onError()
                }
            }
        }

    }
}