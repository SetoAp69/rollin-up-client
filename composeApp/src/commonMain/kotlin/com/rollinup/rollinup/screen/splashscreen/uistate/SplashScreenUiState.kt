package com.rollinup.rollinup.screen.splashscreen.uistate

import com.rollinup.apiservice.model.auth.LoginEntity

data class SplashScreenUiState(
    val isLoading: Boolean = false,
    val loginData: LoginEntity? = null,
    val loginState: Boolean? = null,
)
