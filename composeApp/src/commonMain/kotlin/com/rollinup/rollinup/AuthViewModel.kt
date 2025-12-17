package com.rollinup.rollinup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.domain.auth.ClearClientTokenUseCase
import com.rollinup.apiservice.domain.token.ClearRefreshTokenUseCase
import com.rollinup.apiservice.domain.token.ClearTokenUseCase
import com.rollinup.apiservice.domain.token.UpdateRefreshTokenUseCase
import com.rollinup.apiservice.domain.token.UpdateTokenUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val updateTokenUseCase: UpdateTokenUseCase,
    private val updateRefreshTokenUseCase: UpdateRefreshTokenUseCase,
    private val clearTokenUseCase: ClearTokenUseCase,
    private val clearRefreshTokenUseCase: ClearRefreshTokenUseCase,
    private val clearClientTokenUseCase: ClearClientTokenUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun login(loginData: LoginEntity) {
        val isVerified = loginData.isVerified

        _uiState.update {
            it.copy(
                loginState = if (!isVerified) AuthUiState.LoginState.Unverified else AuthUiState.LoginState.Login,
                loginData = loginData
            )
        }

        with(loginData) {
            viewModelScope.launch {
                if (accessToken.isNotBlank()) updateTokenUseCase(accessToken)
                if (refreshToken.isNotBlank()) updateRefreshTokenUseCase(accessToken)
            }
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                loginState = AuthUiState.LoginState.Logout,
                loginData = null
            )
        }
        viewModelScope.launch {
            clearTokenUseCase()
            clearRefreshTokenUseCase()
            clearClientTokenUseCase()
        }
    }
}