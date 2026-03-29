package com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.uistate

import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.user.OtpStatusEntity
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordErrorType
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordFormData
import com.rollinup.rollinup.screen.auth.model.updatepassword.UpdatePasswordStep

data class VerifyAccountUiState(
    val isLoadingOverlay: Boolean = false,
    val user: LoginEntity? = null,
    val formData: UpdatePasswordFormData = UpdatePasswordFormData(),
    val otp: String = "",
    val otpError: UpdatePasswordErrorType? = null,
    val updatePasswordState: Boolean? = null,
    val submitOtpState: Boolean? = null,
    val requestOtpState: Boolean? = null,
    val countdown: Int = 0,
    val currentStep: UpdatePasswordStep = UpdatePasswordStep.OTP,
    val token: String = "",
    val otpStatus: OtpStatusEntity = OtpStatusEntity(),
)
