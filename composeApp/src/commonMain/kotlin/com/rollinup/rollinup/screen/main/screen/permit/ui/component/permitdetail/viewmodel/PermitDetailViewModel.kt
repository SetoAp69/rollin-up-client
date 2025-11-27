package com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.domain.permit.GetPermitByIdUseCase
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.screen.dashboard.generateDummyPermitDetail
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.uistate.PermitDetailUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PermitDetailViewModel(
    private val getPermitByIdUseCase: GetPermitByIdUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PermitDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun init(id: String) {
        if (id.isNotBlank()) {
            getDetail(id)
        }
    }

    fun reset() {
        _uiState.update { PermitDetailUiState() }
    }

    private fun getDetail(id: String) {
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
}
