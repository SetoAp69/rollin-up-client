package com.rollinup.rollinup.screen.main.screen.setting.ui.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.language.AppLocale
import com.rollinup.rollinup.component.theme.AppLocaleViewModel
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import com.rollinup.rollinup.component.theme.LocalUiModeViewModel
import com.rollinup.rollinup.screen.main.screen.setting.model.SettingCallback

@Composable
fun SettingScreen() {
    val authViewModel = LocalAuthViewmodel.current
    val uiModeViewModel = LocalUiModeViewModel.current
    val appLocaleViewModel = AppLocaleViewModel.current
    val uiMode = uiModeViewModel.uiMode.collectAsStateWithLifecycle().value
    val locale = AppLocale.current

    val cb = SettingCallback(
        onSetUiMode = { uiModeViewModel.updateUiMode(it) },
        onSetLocale = { appLocaleViewModel.setLocale(it) },
        onLogout = { authViewModel.logout() }
    )

    SettingScreenContent(
        uiMode = uiMode,
        locale = locale,
        cb = cb
    )
}