package com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.uistate

import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.rollinup.component.model.User
import com.rollinup.rollinup.screen.auth.model.login.LoginFormData

data class LoginUiState(
    val isLoadingOverlay: Boolean = false,
    val loginFormData: LoginFormData = LoginFormData(),
    val deviceId: String? = null,
    val loginState: Boolean? = null,
    val loginData: LoginEntity? = null
)
