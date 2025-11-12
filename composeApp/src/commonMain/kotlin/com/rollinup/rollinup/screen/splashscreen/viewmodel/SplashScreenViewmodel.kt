package com.rollinup.rollinup.screen.splashscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.domain.auth.LoginJWTUseCase
import com.rollinup.apiservice.model.common.Result
import com.rollinup.apiservice.data.source.datastore.LocalDataStore
import com.rollinup.rollinup.screen.splashscreen.uistate.SplashScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

class SplashScreenViewmodel(
    private val localDataStore: LocalDataStore,
    private val loginJWTUseCase: LoginJWTUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SplashScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun resetState() {
        _uiState.update {
            it.copy(
                loginState = null
            )
        }
    }

    fun auth() {
        viewModelScope.launch {
            val accessToken = localDataStore.getToken()
            if (accessToken.isBlank()) {
                _uiState.update { it.copy(isLoading = false, loginState = false) }
                return@launch
            }

            val jobs = withTimeoutOrNull(700) {
                loginJWTUseCase(accessToken).collectLatest { result ->
                    when (result) {
                        is Result.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    loginState = false
                                )
                            }
                        }

                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    loginState = true,
                                    loginData = result.data
                                )
                            }
                        }
                    }
                }
            }

            if (jobs == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginState = false
                    )
                }
            }
        }
    }
}