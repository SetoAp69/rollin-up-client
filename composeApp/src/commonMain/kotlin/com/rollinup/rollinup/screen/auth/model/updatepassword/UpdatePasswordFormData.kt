package com.rollinup.rollinup.screen.auth.model.updatepassword

import com.rollinup.rollinup.component.password.PasswordErrorType

data class UpdatePasswordFormData(
    val userId: String = "",
    val passwordOne: String = "",
    val passwordTwo: String = "",
    val deviceId: String? = null,

    val passwordOneError: PasswordErrorType? = null,
    val passwordTwoError: PasswordErrorType? = null,
) {
    val isValid
        get() = listOf(passwordTwoError, passwordOneError).all { it == null }
}
