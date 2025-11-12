package com.rollinup.rollinup.screen.auth.model.login

data class LoginFormData(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val accessToken: String? = null,
) {
    val isValid
        get() = listOf(emailError, passwordError).all { it == null }
}
