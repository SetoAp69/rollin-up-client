package com.rollinup.rollinup

import com.rollinup.apiservice.model.auth.LoginEntity

data class AuthUiState(
    val loginState: LoginState? = null,
    val loginData: LoginEntity? = null,
) {
    sealed class LoginState() {
        object Login : LoginState()
        object Logout : LoginState()
    }
}



