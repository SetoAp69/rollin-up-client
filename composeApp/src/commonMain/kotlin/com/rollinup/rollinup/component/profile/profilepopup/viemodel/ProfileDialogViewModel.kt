package com.rollinup.rollinup.component.profile.profilepopup.viemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.domain.user.GetUserByIdUseCase
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.component.profile.profilepopup.uistate.ProfileDialogUiState
import com.rollinup.rollinup.screen.dashboard.generateDummyUserDetail
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileDialogViewModel(
    private val getUserByIdUseCase: GetUserByIdUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileDialogUiState())
    val uiState = _uiState.asStateFlow()

    fun init(id: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            if (true) {
                delay(1000)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userDetail = generateDummyUserDetail()
                    )
                }
                return@launch
            }
            getUserByIdUseCase(id).collectLatest { result ->
                if (result is Result.Success) {
                    _uiState.update { it.copy(userDetail = result.data) }
                }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun reset() {
        _uiState.update { ProfileDialogUiState() }
    }
}