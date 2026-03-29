package com.rollinup.rollinup.screen.auth.model.resetpassword

data class ResetPasswordErrorForm(
    val emailError: ResetPasswordErrorType? = null,
    val firstPasswordError: ResetPasswordErrorType? = null,
    val secondPasswordError: ResetPasswordErrorType? = null,
    val otpError: ResetPasswordErrorType? = null,
)