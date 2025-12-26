package com.rollinup.rollinup.screen.auth.model.updatepassword

data class UpdatePasswordFormData(
    val userId: String = "",
    val passwordOne: String = "",
    val passwordTwo: String = "",
    val deviceId: String? = null,

    val passwordOneError: String? = null,
    val passwordTwoError: String? = null,
) {
    val isValid
        get() = listOf(passwordTwoError, passwordOneError).all { it == null }
}
