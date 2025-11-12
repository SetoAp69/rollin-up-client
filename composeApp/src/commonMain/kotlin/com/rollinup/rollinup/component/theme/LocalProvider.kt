package com.rollinup.rollinup.component.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.apiservice.model.common.GeneralSetting
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.rollinup.AuthViewModel

val LocalTheme = compositionLocalOf<Theme>() {
    error("LocalTheme not found")
}

val LocalAuthViewmodel = compositionLocalOf<AuthViewModel>() {
    error("LocalAuthViewmodel not found")
}

val localUser: LoginEntity?
    @Composable get() = LocalAuthViewmodel.current.uiState.collectAsStateWithLifecycle().value.loginData

val LocalGeneralSetting = compositionLocalOf<GeneralSetting?> {
    error("General Setting not found")
}

val generalSetting
    @Composable get() = LocalGeneralSetting.current
