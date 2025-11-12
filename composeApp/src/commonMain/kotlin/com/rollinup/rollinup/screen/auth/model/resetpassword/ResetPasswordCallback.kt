package com.rollinup.rollinup.screen.auth.model.resetpassword

data class ResetPasswordCallback(
    val onSubmitEmail: (String) -> Unit = {},
    val onSubmitOtp: (String) -> Unit = {},
    val onResetMessageState: () -> Unit = {},
    val onSubmitNewPassword: (String) -> Unit = {},
    val onUpdateStep: (Int) -> Unit = {},
)
