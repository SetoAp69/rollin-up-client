package com.rollinup.rollinup

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michaelflisar.lumberjack.core.L
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import com.rollinup.rollinup.component.theme.LocalGeneralSetting
import com.rollinup.rollinup.component.theme.LocalTheme
import com.rollinup.rollinup.component.theme.RollinUpTheme
import com.rollinup.rollinup.component.theme.Theme
import com.rollinup.rollinup.navigation.NavigationHost
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val isDark = isSystemInDarkTheme()
    val authViewModel: AuthViewModel = koinViewModel()
    val generalSettingViewModel: GeneralSettingViewModel = koinViewModel()

    val authState = authViewModel.uiState.collectAsStateWithLifecycle().value

    DisposableEffect(authState.loginState) {
        L.w { "authstate  = ${authState.loginState}" }
        if (authState.loginState == AuthUiState.LoginState.Login) {
            generalSettingViewModel.fetchLocalSetting()
            generalSettingViewModel.listen()
            L.wtf { "start listening" }
        }
        onDispose {}
    }

    CompositionLocalProvider(
        LocalTheme provides Theme(isDark),
        LocalAuthViewmodel provides authViewModel,
        LocalGeneralSetting provides generalSettingViewModel.generalSetting.collectAsStateWithLifecycle().value

    ) {
        RollinUpTheme {
            NavigationHost()
        }
    }
}