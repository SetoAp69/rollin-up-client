package com.rollinup.rollinup.screen.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Role
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.model.Platform
import com.rollinup.rollinup.component.platformwarning.PlatformWarning
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import com.rollinup.rollinup.component.theme.localUser
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.view.LoginScreen
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view.ResetPasswordScreen
import com.rollinup.rollinup.screen.auth.ui.screen.updatepassword.view.UpdatePasswordScreen

fun NavGraphBuilder.authGraph(
    onNavigateUp: () -> Unit,
    onNavigateTo: (String) -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    composable(route = AuthNavigationRoute.Login.route) {
        val authViewModel = LocalAuthViewmodel.current
        LoginScreen(
            onNavigateTo = onNavigateTo,
            onShowSnackBar = onShowSnackBar,
            onLogout = { authViewModel.logout() }
        ) { loginData ->
            authViewModel.login(loginData)
        }
    }

    composable(route = AuthNavigationRoute.ResetPassword.route) {
        ResetPasswordScreen(
            onNavigateUp = onNavigateUp,
            onShowSnackBar = onShowSnackBar
        )
    }

    composable(route = AuthNavigationRoute.UpdatePassword.route) {
        val authViewModel = LocalAuthViewmodel.current
        val loginData = localUser ?: LoginEntity()
        val platform = getPlatform()

        if (validatePlatform(loginData.role, platform)) {
            UpdatePasswordScreen(
                onSuccess = { authViewModel.logout() },
                onShowSnackBar = onShowSnackBar,
            )
        } else {
            PlatformWarning(loginData.role, platform)
        }
    }

}

private fun validatePlatform(role: Role, platform: Platform): Boolean {
    return role in platform.supportedRole
}