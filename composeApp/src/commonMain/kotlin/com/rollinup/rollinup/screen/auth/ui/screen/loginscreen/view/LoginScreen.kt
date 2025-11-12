package com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.screen.auth.navigation.AuthNavigationRoute
import com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.viewmodel.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    onShowSnackBar: OnShowSnackBar,
    onUpdateLoginData: (LoginEntity) -> Unit,
) {
    val viewModel: LoginViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val cb = viewModel.getCallback()

    DisposableEffect(uiState.loginState) {
        L.wtf { "Disposable Effect triggerred loginstate : ${uiState.loginState} " }
        uiState.loginState?.let {
            if (it) {
                onUpdateLoginData(uiState.loginData!!)
                navController.navigate(com.rollinup.rollinup.navigation.NavigationRoute.TestRoute.route) {
                    popUpTo(AuthNavigationRoute.Login.route) {
                        inclusive = true
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
        onNavigateTo = { navController.navigate(it) },
        uiState = uiState,
        cb = cb,
        onShowSnackBar = onShowSnackBar
    )
}