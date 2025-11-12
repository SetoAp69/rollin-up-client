package com.rollinup.rollinup.screen.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.view.LoginScreen
import com.rollinup.rollinup.screen.auth.ui.screen.resetpassword.view.ResetPasswordScreen

fun NavGraphBuilder.authGraph(
    navController: NavController,
    onShowSnackBar: OnShowSnackBar,
) {
    val onNavigateUp: () -> Unit = { navController.popBackStack() }
    val onNavigateTo: (String) -> Unit = { navController.navigate(it) }

    composable(route = AuthNavigationRoute.Login.route) {
        val authViewModel = LocalAuthViewmodel.current
        LoginScreen(
            navController = navController,
            onShowSnackBar = onShowSnackBar
        ) {
            authViewModel.login(it)
        }
    }

    composable(route = AuthNavigationRoute.ResetPassword.route) {
        ResetPasswordScreen(
            onNavigateUp = onNavigateUp,
            onShowSnackBar = onShowSnackBar
        )
    }
}