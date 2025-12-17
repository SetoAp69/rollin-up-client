package com.rollinup.rollinup.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.rollinup.AuthUiState
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import com.rollinup.rollinup.component.theme.localUser
import com.rollinup.rollinup.screen.auth.navigation.AuthNavigationRoute


@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    val loginState = LocalAuthViewmodel.current.uiState.value.loginState
    val loginData = localUser
    val authViewmodel = LocalAuthViewmodel.current

    AppNavHost(
        navController = navController,
        loginData = loginData,
        initialRoute = getInitialRoute(
            loginState = loginState,
            loginData = loginData
        )
    ) {
        authViewmodel.logout()
        navController.navigate(NavigationRoute.Auth.route)
        navController.popBackStack(
            inclusive = true,
            route = navController.previousBackStackEntry?.destination?.route ?: ""
        )
    }
}

private fun getInitialRoute(loginState: AuthUiState.LoginState?, loginData: LoginEntity?): String {
    return when (loginState) {
        AuthUiState.LoginState.Login -> NavigationRoute.MainRoute.navigate(loginData?.role!!)
        AuthUiState.LoginState.Logout -> NavigationRoute.Auth.route
        AuthUiState.LoginState.Unverified -> AuthNavigationRoute.UpdatePassword.route
        null -> { NavigationRoute.SplashScreen.route }
    }
}
