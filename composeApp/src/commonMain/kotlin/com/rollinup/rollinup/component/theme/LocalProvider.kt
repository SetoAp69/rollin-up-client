package com.rollinup.rollinup.component.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.GlobalSetting
import com.rollinup.common.model.UiMode
import com.rollinup.rollinup.AuthViewModel
import com.rollinup.rollinup.UiModeViewModel
import kotlinx.datetime.LocalDate

val LocalTheme = compositionLocalOf<Theme>() {
    error("LocalTheme not found")
}

val LocalAuthViewmodel = compositionLocalOf<AuthViewModel>() {
    error("LocalAuthViewmodel not found")
}

val LocalUiModeViewModel = compositionLocalOf<UiModeViewModel> {
    error("LocalUiModeViewModel not found")
}

val localUser: LoginEntity?
    @Composable get() = LocalAuthViewmodel.current.uiState.collectAsStateWithLifecycle().value.loginData

val LocalGeneralSetting = compositionLocalOf<GlobalSetting> {
    error("General Setting not found")
}

val LocalHolidayProvider = compositionLocalOf<List<LocalDate>> {
    emptyList()
}

val LocalUiMode = compositionLocalOf<UiMode> {
    error("UiMode not found")
}

val generalSetting
    @Composable get() = LocalGeneralSetting.current
