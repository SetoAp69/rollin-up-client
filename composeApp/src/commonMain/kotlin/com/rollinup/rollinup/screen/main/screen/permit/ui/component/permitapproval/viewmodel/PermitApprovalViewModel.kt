package com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitapproval.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.data.source.network.model.request.permit.PermitApprovalBody
import com.rollinup.apiservice.domain.permit.DoApprovalUseCase
import com.rollinup.apiservice.domain.permit.GetPermitByIdUseCase
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.screen.dashboard.generateDummyPermitDetail
import com.rollinup.rollinup.screen.main.screen.permit.model.permitapproval.PermitApprovalCallback
import com.rollinup.rollinup.screen.main.screen.permit.model.permitapproval.PermitApprovalFormData
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitapproval.uistate.PermitApprovalUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PermitApprovalViewModel(
    private val getPermitByIdUseCase: GetPermitByIdUseCase,
    private val doApprovalUseCase: DoApprovalUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PermitApprovalUiState())
    val uiState = _uiState.asStateFlow()

    fun init(listId: List<String>) {
        _uiState.update { it.copy(selectedId = listId) }

        if (uiState.value.isSingle) {
            getInitialData(listId.first())
        }
    }

    fun getCallback() =
        PermitApprovalCallback(
            onValidate = ::validate,
            onSubmit = ::submit,
            onUpdateFormData = ::updateFormData,
            onResetMessageState = ::resetMessageState
        )

    private fun getInitialData(id: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            if(true){
                delay(1000)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        detail = generateDummyPermitDetail()
                    )
                }
            }
            getPermitByIdUseCase(id).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                detail = result.data
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    private fun updateFormData(formData: PermitApprovalFormData) {
        _uiState.update { it.copy(formData = formData) }
    }

    private fun validate(formData: PermitApprovalFormData): Boolean {
        return formData.isValid
    }

    private fun submit(formData: PermitApprovalFormData) {
        val body = PermitApprovalBody(
            listId = uiState.value.selectedId,
            approvalNote = formData.approvalNote,
            isApproved = formData.isApproved
        )
        _uiState.update { it.copy(isLoadingOverlay = true) }
        viewModelScope.launch {
            doApprovalUseCase(body).collectLatest { result ->
                _uiState.update {
                    it.copy(
                        isLoadingOverlay = false,
                        submitState = result is Result.Success
                    )
                }
            }
        }
    }

    private fun resetMessageState() {
        _uiState.update { it.copy(submitState = null) }
    }

    fun reset() {
        _uiState.update { PermitApprovalUiState() }
    }
}