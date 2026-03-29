package com.rollinup.rollinup.screen.main.screen.setting.model

import com.rollinup.common.model.LocaleEnum
import com.rollinup.common.model.UiMode

data class SettingCallback(
    val onSetUiMode: (UiMode) -> Unit = {},
    val onSetLocale: (LocaleEnum) -> Unit = {},
    val onLogout: () -> Unit = {},
    val onProfile: () -> Unit = {},
)
