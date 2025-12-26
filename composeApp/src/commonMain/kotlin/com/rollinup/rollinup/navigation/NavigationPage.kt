package com.rollinup.rollinup.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import com.rollinup.rollinup.screen.auth.navigation.AuthNavigationRoute
import com.rollinup.rollinup.screen.auth.navigation.authGraph
import com.rollinup.rollinup.screen.main.navigation.MainRoute
import com.rollinup.rollinup.screen.main.navigation.mainGraph
import com.rollinup.rollinup.screen.splashscreen.SplashScreen


fun NavGraphBuilder.appGraph(
    navController: NavHostController,
    onShowSnackBar: (String, Boolean) -> Unit,
    onRefreshSetting: () -> Unit,
) {
    fun navigateTo(route: String) {
        navController.navigate(route)
    }

    fun navigateUp() {
        navController.popBackStack()
    }

    composable(NavigationRoute.SplashScreen.route) {
        val authViewModel = LocalAuthViewmodel.current
        SplashScreen(
            onLogin = { loginData ->
                authViewModel.login(loginData)
            },
            onGoToLogin = {
                navController.navigate(AuthNavigationRoute.Login.route) {
                    popUpTo(NavigationRoute.SplashScreen.route) {
                        inclusive = true
                    }
                }
            }
        )
    }

    navigation(
        startDestination = AuthNavigationRoute.Login.route,
        route = NavigationRoute.Auth.route
    ) {
        authGraph(
            navController = navController,
            onShowSnackBar = onShowSnackBar,
        )
    }

    navigation(
        route = NavigationRoute.MainRoute.route,
        startDestination = MainRoute.DashBoardRoute.route
    ) {
        mainGraph(
            onNavigateTo = ::navigateTo,
            onShowSnackBar = onShowSnackBar,
            onNavigateUp = ::navigateUp,
            onRefreshSetting = onRefreshSetting
        )
    }

    authGraph(
        navController = navController,
        onShowSnackBar = onShowSnackBar,
    )

}

