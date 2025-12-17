package com.rollinup.rollinup.screen.auth.model.updatepassword

data class UpdatePasswordCallback(
    val onUpdateFormData: (UpdatePasswordFormData) -> Unit = {},
    val onSubmit: (UpdatePasswordFormData) -> Unit = {},
    val onSubmitOtp: (String) -> Unit = {},
    val onUpdateOtp: (String) -> Unit = {},
    val onResendOtp: () -> Unit = {},
    val onResetForm: () -> Unit = {},
    val onResetMessageState: () -> Unit = {},
)
