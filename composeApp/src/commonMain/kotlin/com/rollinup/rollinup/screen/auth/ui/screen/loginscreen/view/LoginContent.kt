package com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.model.Platform
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.auth.model.login.LoginCallback
import com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.uistate.LoginUiState

@Composable
fun LoginContent(
    onNavigateTo: (String) -> Unit,
    uiState: LoginUiState,
    cb: LoginCallback,
    onShowSnackBar: OnShowSnackBar,
) {
    Scaffold(
        topBar = {
            LoginTopAppBar(
                onInfoClick = {}
            )
        },
        showLoadingOverlay = uiState.isLoadingOverlay
    ) {
        LoginContent(
            uiState = uiState,
            cb = cb,
            onNavigateTo = onNavigateTo
        )
    }
}


@Composable
fun LoginContent(
    uiState: LoginUiState,
    cb: LoginCallback,
    onNavigateTo: (String) -> Unit,
) {
    val platform = getPlatform()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (platform) {
            Platform.ANDROID, Platform.IOS -> {
                LoginContentCompact(
                    onNavigateTo = onNavigateTo,
                    uiState = uiState,
                    cb = cb
                )
            }

            else -> {
                LoginContentWide(
                    uiState = uiState,
                    cb = cb,
                    onNavigateTo = onNavigateTo
                )
            }

        }
    }
}