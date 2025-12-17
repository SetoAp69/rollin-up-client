package com.rollinup.rollinup.screen.main.screen.setting.ui.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import com.rollinup.rollinup.component.theme.LocalUiModeViewModel

@Composable
fun SettingScreen() {
    val authViewModel = LocalAuthViewmodel.current
    val uiModeViewModel = LocalUiModeViewModel.current
    val uiMode = uiModeViewModel.uiMode.collectAsStateWithLifecycle().value

    SettingContent(
        uiMode = uiMode,
        onLogout = { authViewModel.logout() },
        onUiModeChange = { uiMode -> uiModeViewModel.updateUiMode(uiMode) }
    )
}