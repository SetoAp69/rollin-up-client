package com.rollinup.rollinup

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.common.model.SecurityAlert
import com.rollinup.common.model.Severity
import com.rollinup.common.model.UiMode
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import com.rollinup.rollinup.component.theme.LocalGlobalSetting
import com.rollinup.rollinup.component.theme.LocalTheme
import com.rollinup.rollinup.component.theme.LocalUiModeViewModel
import com.rollinup.rollinup.component.theme.RollinUpTheme
import com.rollinup.rollinup.component.theme.Theme
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.toAnnotatedString
import com.rollinup.rollinup.navigation.NavigationHost
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.msg_multiple_security_alert

@Composable
fun App(
    onFinish: () -> Unit,
) {
    val authViewModel: AuthViewModel = koinViewModel()
    val generalSettingViewModel: GlobalSettingViewModel = koinViewModel()
    val uiModeViewModel: UiModeViewModel = koinViewModel()
    val securityViewModel: SecurityViewModel = koinViewModel()
    val uiMode = uiModeViewModel.uiMode.collectAsStateWithLifecycle().value
    val authState = authViewModel.uiState.collectAsStateWithLifecycle().value
    val securityAlerts = securityViewModel.securityAlert.collectAsStateWithLifecycle().value
    val globalSetting = generalSettingViewModel.globalSetting.collectAsStateWithLifecycle().value

    DisposableEffect(authState.loginState) {
        if (authState.loginState == AuthUiState.LoginState.Login) {
            generalSettingViewModel.fetchLocalSetting()
            generalSettingViewModel.listen()
        }
        onDispose {}
    }

    LaunchedEffect(Unit) {
        uiModeViewModel.getUiMode()
        generalSettingViewModel.init()
    }

    CompositionLocalProvider(
        LocalTheme provides generateTheme(uiMode),
        LocalUiModeViewModel provides uiModeViewModel,
        LocalAuthViewmodel provides authViewModel,
        LocalGlobalSetting provides globalSetting
    ) {
        RollinUpTheme {
            NavigationHost(
                onRefreshSetting = { generalSettingViewModel.init() },
                onLogout = { authViewModel.logout() }
            )
            SecurityAlertDialog(
                showDialog = securityAlerts.isNotEmpty(),
                securityAlert = securityAlerts,
                onDismissRequest = { onFinish() }
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

@Composable
fun SecurityAlertDialog(
    showDialog: Boolean,
    securityAlert: List<SecurityAlert>,
    onDismissRequest: (Boolean) -> Unit,
) {
    val title = getAlertTitle(securityAlert)
    val message = getAlertMessage(securityAlert)

    AlertDialog(
        title = title,
        content = message,
        onDismissRequest = onDismissRequest,
        onClickCancel = { onDismissRequest(false) },
        onClickConfirm = {},
        isSingleButton = true,
        showCancelButton = true,
        isShowDialog = showDialog,
        icon = Res.drawable.ic_info_line_24,
        iconTint = theme.danger,
        severity = Severity.DANGER
    )
}

fun getAlertTitle(securityAlert: List<SecurityAlert>) =
    if (securityAlert.size == 1) "Security Alert : ${securityAlert.firstOrNull()?.title ?: ""}"
    else ""

@Composable
fun getAlertMessage(securityAlert: List<SecurityAlert>): AnnotatedString {
    return if (securityAlert.size == 1) {
        (securityAlert.firstOrNull()?.message ?: "").toAnnotatedString()
    } else {
        stringResource(Res.string.msg_multiple_security_alert).toAnnotatedString()
    }
}