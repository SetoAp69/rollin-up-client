package com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Role
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.utils.getDeviceId
import com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.viewmodel.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onNavigateTo: (String) -> Unit,
    onShowSnackBar: OnShowSnackBar,
    onLogout: () -> Unit,
    onLogin: (LoginEntity) -> Unit,
) {
    val viewModel: LoginViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val cb = viewModel.getCallback()

    var showUnauthorizedDeviceDialog by remember { mutableStateOf(false) }
    var showFirstTimeLoginDialog by remember { mutableStateOf(false) }

    val loginData = uiState.loginData
    val deviceId = getDeviceId()


    DisposableEffect(uiState.loginState) {
        uiState.loginState?.let {
            val isFirstTimeLogin = loginData?.isVerified == false
            val deviceIsNotValid =
                !validateDevice(loginData = loginData, deviceId = deviceId)

            if (it) {
                when {
                    isFirstTimeLogin -> {
                        showFirstTimeLoginDialog = true
                    }

                    deviceIsNotValid -> {
                        showUnauthorizedDeviceDialog = true
                    }

                    else -> {
                        onLogin(loginData!!)
                    }
                }
            } else {
                onShowSnackBar("Error, Login failed", false)
            }
        }

        onDispose {
            cb.onResetMessageState()
        }
    }

    LoginContent(
        onNavigateTo = onNavigateTo,
        uiState = uiState,
        cb = cb,
    )

    UnauthorizedDeviceDialog(
        showDialog = showUnauthorizedDeviceDialog,
        onDismissRequest = { showUnauthorizedDeviceDialog = it },
        onClickConfirm = {
            onLogout()
            viewModel.reset()
        }
    )

    uiState.loginData?.let {
        FirstTimeLoginDialog(
            showDialog = showFirstTimeLoginDialog,
            onDismissRequest = { showFirstTimeLoginDialog = it },
            onClickConfirm = {
                onLogin(it)
            }
        )
    }
}

private fun validateDevice(loginData: LoginEntity?, deviceId: String): Boolean {
    if (loginData == null) return false

    return loginData.role != Role.STUDENT || loginData.deviceId.isBlank() || loginData.deviceId == deviceId
}

