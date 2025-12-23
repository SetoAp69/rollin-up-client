package com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.apiservice.data.source.network.model.request.user.SubmitVerificationOTPBody
import com.rollinup.apiservice.data.source.network.model.request.user.UpdatePasswordAndVerificationBody
import com.rollinup.apiservice.domain.user.ResendVerificationOtpUseCase
import com.rollinup.apiservice.domain.user.SubmitVerificationOtpUseCase
import com.rollinup.apiservice.domain.user.UpdatePasswordAndVerificationUseCase
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Result
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordCallback
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordFormData
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordStep
import com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.uistate.UpdatePasswordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpdatePasswordViewModel(
    private val updatePasswordAndVerificationUseCase: UpdatePasswordAndVerificationUseCase,
    private val submitVerificationOtpUseCase: SubmitVerificationOtpUseCase,
    private val resendVerificationOtpUseCase: ResendVerificationOtpUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UpdatePasswordUiState())
    val uiState = _uiState.asStateFlow()

    fun init(user: LoginEntity?) {
        _uiState.update {
            it.copy(
                user = user,
                startTimer = true
            )
        }
    }

    fun getCallback() =
        UpdatePasswordCallback(
            onUpdateFormData = ::updateFormData,
            onSubmit = ::submitUpdatePassword,
            onSubmitOtp = ::submitOTP,
            onUpdateOtp = ::updateOtp,
            onResendOtp = ::resendOtp,
            onResetOtp = ::resetForm,
            onResetMessageState = ::resetMessageState
        )

    fun reset() {
        _uiState.value = UpdatePasswordUiState()
    }

    fun resetForm() {
        _uiState.update {
            it.copy(
                otp = "",
                otpError = null,
            )
        }
    }

    private fun updateOtp(otp: String) {
        _uiState.update { it.copy(otp = otp) }
    }

    private fun updateFormData(formData: UpdatePasswordFormData) {
        _uiState.update { it.copy(formData = formData) }
    }

    private fun resendOtp() {
        _uiState.update { it.copy(isLoadingOverlay = true, requestOtpState = null) }
        viewModelScope.launch {
            resendVerificationOtpUseCase().collectLatest { result ->
                _uiState.update {
                    it.copy(
                        isLoadingOverlay = false,
                        requestOtpState = result is Result.Success,
                        startTimer = true
                    )
                }
            }
        }
    }

    private fun validateOtp(otp: String): Boolean {
        val otpError = when {
            otp.isBlank() -> "OTP cannot be empty"
            otp.length < 5 -> "You need to fills all the digits"
            else -> null
        }
        _uiState.update {
            it.copy(
                otpError = otpError
            )
        }
        return otpError == null
    }

    private fun submitOTP(otp: String) {
        if (!validateOtp(otp)) return
        _uiState.update {
            it.copy(
                isLoadingOverlay = true,
                submitOtpState = null,
                startTimer = false
            )
        }
        viewModelScope.launch {
            submitVerificationOtpUseCase(SubmitVerificationOTPBody(otp)).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoadingOverlay = false,
                                token = result.data,
                                submitOtpState = true,
                                currentStep = UpdatePasswordStep.UPDATE_PASSWORD
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
            }
        }
    }

    private fun submitUpdatePassword(formData: UpdatePasswordFormData) {

        _uiState.update {
            it.copy(isLoadingOverlay = true)
        }
//        updatePasswordAndVerificationUseCase(
//            UpdatePasswordAndVerificationBody(
//                formData.passwordOne,
//                formData.deviceId,
//                _uiState.value.token
//            )
//        ).onEach { result ->
//            when (result) {
//                is Result.Success -> {
//                    _uiState.update {
//                        it.copy(
//                            isLoadingOverlay = false,
//                            updatePasswordState = true
//                        )
//                    }
//                }
//
//                is Result.Error->{
//                    _uiState.update {
//                        it.copy(
//                            isLoadingOverlay = false,
//                            updatePasswordState = false
//                        )
//                    }
//                }
//            }
//        }.launchIn(viewModelScope)
        viewModelScope.launch {
            _uiState.update { it.copy(updatePasswordState = null) }
            val body = UpdatePasswordAndVerificationBody(
                password = formData.passwordOne,
                deviceId = formData.deviceId,
                token = uiState.value.token
            )
            _uiState.update { it.copy(isLoadingOverlay = true) }
            updatePasswordAndVerificationUseCase(body).collectLatest { result ->
               when (result){
                   is Result.Success->{
                       _uiState.update {
                           it.copy(
                               isLoadingOverlay = false,
                               updatePasswordState = true
                           )
                       }
                   }

                   is Result.Error->{
                       _uiState.update {
                           it.copy(
                               isLoadingOverlay = false,
                               updatePasswordState = false
                           )
                       }
                   }
               }

            }
        }
    }

    private fun resetMessageState() {
        _uiState.update {
            it.copy(
                submitOtpState = null,
                requestOtpState = null,
                updatePasswordState = null
            )
        }
    }


}