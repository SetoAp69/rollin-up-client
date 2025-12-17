package com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.data.source.network.model.request.auth.LoginBody
import com.rollinup.apiservice.domain.auth.LoginUseCase
import com.rollinup.apiservice.domain.auth.ClearClientTokenUseCase
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.screen.auth.model.login.LoginCallback
import com.rollinup.rollinup.screen.auth.model.login.LoginFormData
import com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.uistate.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: ClearClientTokenUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun getCallback() = LoginCallback(
        onUpdateForm = ::updateForm,
        onLogin = ::login,
    )

    fun reset() {
        _uiState.value = LoginUiState()
    }


    private fun updateForm(formData: LoginFormData) {
        _uiState.update {
            it.copy(
                loginFormData = formData
            )
        }
    }

    private fun validateLoginFormData(
        formData: LoginFormData,
    ): Boolean {
        var formData = formData

        if (formData.email.isEmpty()) {
            formData = formData.copy(
                emailError = "Email/username can't be empty"
            )
        }

        if (formData.password.isEmpty()) {
            formData = formData.copy(
                passwordError = "Password can't be empty"
            )
        }

        updateForm(formData)
        return formData.isValid
    }

    private fun login(loginFormData: LoginFormData) {
        if (!validateLoginFormData(formData = loginFormData)) return

        _uiState.update {
            it.copy(
                isLoadingOverlay = true,
                loginState = null
            )
        }

        val body = LoginBody(
            email = loginFormData.email,
            password = loginFormData.password,
        )

        loginUseCase(body).onEach { result ->
            _uiState.update { it.copy(loginState = null) }
            val isSuccess = result is Result.Success

            when (result) {
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingOverlay = false,
                            loginState = false
                        )
                    }
                }

                is Result.Success -> {
                    logoutUseCase()
                    val loginData = result.data
                    _uiState.update {
                        it.copy(
                            isLoadingOverlay = false,
                            loginData = loginData,
                            loginState = true
                        )
                    }
                }
            }

            _uiState.update {
                it.copy(
                    loginState = isSuccess,
                    isLoadingOverlay = false
                )
            }

        }.launchIn(viewModelScope)
    }

}