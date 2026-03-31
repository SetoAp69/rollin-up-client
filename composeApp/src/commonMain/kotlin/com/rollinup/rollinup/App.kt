package com.rollinup.rollinup

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.common.model.UiMode
import com.rollinup.rollinup.component.language.AppLocale
import com.rollinup.rollinup.component.theme.AppLocaleViewModel
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import com.rollinup.rollinup.component.theme.LocalGlobalSetting
import com.rollinup.rollinup.component.theme.LocalTheme
import com.rollinup.rollinup.component.theme.LocalUiModeViewModel
import com.rollinup.rollinup.component.theme.RollinUpTheme
import com.rollinup.rollinup.component.theme.Theme
import com.rollinup.rollinup.navigation.NavigationHost
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    authViewModel: AuthViewModel = koinViewModel(),
    onFinish: () -> Unit,
) {
    val generalSettingViewModel: GlobalSettingViewModel = koinViewModel()
    val uiModeViewModel: UiModeViewModel = koinViewModel()
    val localeViewModel: LocaleViewModel = koinViewModel()

    val uiMode = uiModeViewModel.uiMode.collectAsStateWithLifecycle().value
    val authState = authViewModel.uiState.collectAsStateWithLifecycle().value
    val locale = localeViewModel.locale.collectAsStateWithLifecycle().value

    DisposableEffect(authState.loginState) {
        if (authState.loginState == AuthUiState.LoginState.Login) {
            generalSettingViewModel.fetchLocalSetting()
            generalSettingViewModel.listen()
        }
        onDispose {}
    }
    val globalSetting = generalSettingViewModel.globalSetting.collectAsStateWithLifecycle().value

    val globalSettingInitState =
        generalSettingViewModel.initState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        uiModeViewModel.getUiMode()
        generalSettingViewModel.init()
    }

    LaunchedEffect(globalSettingInitState) {
        if (globalSettingInitState == false) {
            generalSettingViewModel.init()
        }
    }

    CompositionLocalProvider(
        AppLocale provides locale,
        LocalTheme provides generateTheme(uiMode),
        LocalUiModeViewModel provides uiModeViewModel,
        LocalAuthViewmodel provides authViewModel,
        AppLocaleViewModel provides localeViewModel,
        LocalGlobalSetting provides globalSetting
    ) {
        RollinUpTheme {
            NavigationHost(
                onRefreshSetting = { generalSettingViewModel.init() },
                onLogout = { authViewModel.logout() },
                onFinish = onFinish
            )
        }
    }
}

@Composable
private fun generateTheme(
    uiMode: UiMode,
): Theme {
    val isDark = when (uiMode) {
        UiMode.DARK -> true
        UiMode.LIGHT -> false
        UiMode.AUTO -> isSystemInDarkTheme()
    }
    return Theme(isDark)
}