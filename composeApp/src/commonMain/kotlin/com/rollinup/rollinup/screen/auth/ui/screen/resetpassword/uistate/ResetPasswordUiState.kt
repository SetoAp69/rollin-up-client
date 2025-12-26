package com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.uistate

data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val submitEmailState: Boolean? = null,
    val submitOtpState: Boolean? = null,
    val submitPasswordState: Boolean? = null,
    val step: Int = 0,
    val email: String = "",
    val emailError: String? = null,
    val otp: Int? = null,
    val otpError: String? = null,
    val actualEmail: String? = null,
    val newPassword: String = "",
    val resetToken: String = "",
)
