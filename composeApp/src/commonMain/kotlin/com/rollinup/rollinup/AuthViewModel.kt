package com.rollinup.rollinup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val localDataStore: LocalDataStore,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun login(loginData: LoginEntity) {
        _uiState.update {
            it.copy(
                loginState = AuthUiState.LoginState.Login,
                loginData = loginData
            )
        }
        with(loginData){
            viewModelScope.launch {
                if(accessToken.isNotBlank()) localDataStore.updateToken(accessToken)
                if(refreshToken.isNotBlank()) localDataStore.updateRefreshToken(refreshToken)
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
            localDataStore.clearToken()
            localDataStore.clearRefreshToken()
        }
    }
}