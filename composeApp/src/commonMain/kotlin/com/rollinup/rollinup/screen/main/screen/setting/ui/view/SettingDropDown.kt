package com.rollinup.rollinup.screen.main.screen.setting.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michaelflisar.lumberjack.core.L
import com.rollinup.common.model.LocaleEnum
import com.rollinup.common.model.Severity
import com.rollinup.common.model.UiMode
import com.rollinup.rollinup.component.button.IconButton
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.dropdown.DropDownMenuItem
import com.rollinup.rollinup.component.language.AppLocale
import com.rollinup.rollinup.component.selector.SingleSelector
import com.rollinup.rollinup.component.theme.AppLocaleViewModel
import com.rollinup.rollinup.component.theme.LocalUiModeViewModel
import com.rollinup.rollinup.screen.main.screen.setting.model.SettingCallback
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_down_24
import rollin_up.composeapp.generated.resources.ic_exit_line_24
import rollin_up.composeapp.generated.resources.ic_translate_line_24
import rollin_up.composeapp.generated.resources.ic_user_line_24
import rollin_up.composeapp.generated.resources.label_language
import rollin_up.composeapp.generated.resources.label_logout
import rollin_up.composeapp.generated.resources.label_profile

@Composable
fun SettingDropDown(
    onLogout: () -> Unit,
    onShowProfile: () -> Unit,
) {
    var isShowDropDown by remember { mutableStateOf(false) }

    val uiModeViewModel = LocalUiModeViewModel.current
    val appLocaleViewModel = AppLocaleViewModel.current
    val locale = AppLocale.current
    val uiMode = uiModeViewModel.uiMode.collectAsStateWithLifecycle().value

    L.wtf { "Current Locale : ${Locale.current}" }

    val callBack = SettingCallback(
        onSetUiMode = { uiModeViewModel.updateUiMode(it) },
        onSetLocale = { appLocaleViewModel.setLocale(it) },
        onLogout = onLogout,
        onProfile = onShowProfile
    )

    Box {
        IconButton(
            icon = Res.drawable.ic_drop_down_arrow_line_down_24,
            severity = Severity.PRIMARY,
            size = 16.dp,
            onClick = {
                isShowDropDown = !isShowDropDown
            }
        )

        SettingDropDownContent(
            locale = locale,
            uiMode = uiMode,
            cb = callBack,
            isShowDropDown = isShowDropDown,
            onDismissRequest = { isShowDropDown = it }
        )
    }
}

@Composable
private fun SettingDropDownContent(
    locale: LocaleEnum,
    uiMode: UiMode,
    cb: SettingCallback,
    isShowDropDown: Boolean,
    onDismissRequest: (Boolean) -> Unit,
) {
    DropDownMenu(
        isShowDropDown = isShowDropDown,
        onDismissRequest = onDismissRequest,
    ) {
        UiModeDropDownSetting(
            uiMode = uiMode,
            onUiModeChanges = cb.onSetUiMode
        )
        DropDownMenuItem(
            label = stringResource(Res.string.label_profile),
            onClick = cb.onProfile,
            icon = Res.drawable.ic_user_line_24
        )
        LocaleSetting(
            onLocaleChanges = cb.onSetLocale,
            locale = locale
        )
        DropDownMenuItem(
            label = stringResource(Res.string.label_logout),
            onClick = cb.onLogout,
            icon = Res.drawable.ic_exit_line_24
        )
    }
}

@Composable
private fun UiModeDropDownSetting(
    uiMode: UiMode,
    onUiModeChanges: (UiMode) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        UiModeSwitch(
            value = uiMode,
            onValueChanges = onUiModeChanges
        )
    }
}

@Composable
private fun LocaleSetting(
    locale: LocaleEnum,
    onLocaleChanges: (LocaleEnum) -> Unit,
) {
    var isShowSelector by remember { mutableStateOf(false) }
    val options = LocaleEnum.getOptions()
    Box {
        DropDownMenuItem(
            label = locale.label,
            onClick = { isShowSelector = true },
            icon = Res.drawable.ic_translate_line_24
        )
        SingleSelector(
            isShowSelector = isShowSelector,
            onDismissRequest = { isShowSelector = it },
            title = stringResource(Res.string.label_language),
            value = locale,
            options = options,
            onValueChange = onLocaleChanges
        )
    }
}