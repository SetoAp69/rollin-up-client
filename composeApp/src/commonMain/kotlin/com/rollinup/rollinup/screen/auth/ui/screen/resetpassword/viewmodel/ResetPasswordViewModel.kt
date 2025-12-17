package com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.data.source.network.model.request.user.CreateResetPasswordRequestBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitResetPasswordBody
import com.rollinup.apiservice.domain.user.CreateResetPasswordRequestUseCase
import com.rollinup.apiservice.domain.user.SubmitResetOtpUseCase
import com.rollinup.apiservice.domain.user.SubmitResetPasswordUseCase
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.screen.auth.model.resetpassword.ResetPasswordCallback
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.uistate.ResetPasswordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ResetPasswordViewModel(
    private val createResetPasswordRequestUseCase: CreateResetPasswordRequestUseCase,
    private val submitResetOtpUseCase: SubmitResetOtpUseCase,
    private val submitResetPasswordUseCase: SubmitResetPasswordUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState = _uiState.asStateFlow()

    fun getCallback() = ResetPasswordCallback(
        onSubmitEmail = ::submitEmail,
        onSubmitOtp = ::submitOtp,
        onResetMessageState = ::resetMessageState,
        onSubmitNewPassword = ::submitNewPassword,
        onUpdateStep = ::updateStep
    )

    private fun submitEmail(email: String) {
        _uiState.update {
            it.copy(isLoadingOverlay = true)
        }
        val body = CreateResetPasswordRequestBody(email = email)
        createResetPasswordRequestUseCase(body).onEach { result ->
            when (result) {
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingOverlay = false,
                            submitEmailState = false
                        )
                    }
                }

                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingOverlay = false,
                            submitEmailState = true,
                            email = email,
                            actualEmail = result.data,
                            step = it.step + 1
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun submitOtp(otp: String) {
        _uiState.update {
            it.copy(isLoadingOverlay = true)
        }

        val body = SubmitResetPasswordOTPBody(
            email = _uiState.value.email,
            otp = otp
        )

        submitResetOtpUseCase(body).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingOverlay = false,
                            submitOtpState = true,
                            step = it.step + 1,
                            resetToken = result.data
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingOverlay = false,
                            submitOtpState = false
                        )
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

    private fun submitNewPassword(password: String) {
        _uiState.update {
            it.copy(isLoadingOverlay = true)
        }

        val body = SubmitResetPasswordBody(
            token = _uiState.value.resetToken,
            newPassword = password
        )

        submitResetPasswordUseCase(body).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoadingOverlay = false,
                            submitPasswordState = true
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingOverlay = false,
                            submitPasswordState = false
                        )
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

    private fun resetMessageState() {
        _uiState.update {
            it.copy(
                submitEmailState = null,
                submitOtpState = null,
                submitPasswordState = null
            )
        }
    }

    private fun updateStep(step: Int) {
        _uiState.update {
            it.copy(
                step = step
            )
        }
    }
}