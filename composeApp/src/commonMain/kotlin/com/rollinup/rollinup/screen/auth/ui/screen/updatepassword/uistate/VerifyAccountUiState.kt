package com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.uistate

import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordFormData
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordStep

data class VerifyAccountUiState(
    val isLoadingOverlay: Boolean = false,
    val user: LoginEntity? = null,
    val formData: UpdatePasswordFormData = UpdatePasswordFormData(),
    val otp: String = "",
    val otpError: String? = null,
    val updatePasswordState: Boolean? = null,
    val submitOtpState: Boolean? = null,
    val requestOtpState: Boolean? = null,
    val startTimer: Boolean = false,
    val currentStep: UpdatePasswordStep = UpdatePasswordStep.OTP,
    val token: String = "",
)
