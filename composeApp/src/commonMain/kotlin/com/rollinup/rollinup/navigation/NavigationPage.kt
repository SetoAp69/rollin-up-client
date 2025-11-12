package com.rollinup.rollinup.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rollinup.apiservice.model.common.Role
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import com.rollinup.rollinup.screen.auth.navigation.authGraph
import com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.view.LoginScreen
import com.rollinup.rollinup.screen.splashscreen.SplashScreen
import com.rollinup.rollinup.screen.test.ui.view.TestScreen


fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    onShowSnackBar: (String, Boolean) -> Unit,
) {

    val onNavigateUp: () -> Unit = { navController.popBackStack() }
    val onNavigateTo: (String) -> Unit = { navController.navigate(it) }

    composable(NavigationRoute.TestRoute.route) {
        val authViewModel = LocalAuthViewmodel.current

        TestScreen(
            onShowSnackBar = onShowSnackBar,
            onNavigateUp = { navController.popBackStack() },
            navController = navController,
            onUpdateLoginData = {
                authViewModel.logout()
            }
        )
    }

    composable(NavigationRoute.Auth.route) {
        val authViewModel = LocalAuthViewmodel.current
        LoginScreen(
            navController = navController,
            onShowSnackBar = onShowSnackBar
        ) {
            authViewModel.login(it)
        }
    }

    composable(NavigationRoute.SplashScreen.route) {
        val authViewModel = LocalAuthViewmodel.current

        SplashScreen(navController = navController) { loginData ->
            authViewModel.login(loginData)
        }
    }

    composable(
        route = NavigationRoute.Dashboard.route,
        arguments = listOf(
            navArgument(
                name = "role",
                builder = {
                    type = NavType.StringType
                }
            )
        )
    ) { navBackStackEntry ->
        val role = navBackStackEntry.savedStateHandle.get<String>("role").let {
            Role.fromValue(it.toString())
        }

        when (role) {
            Role.ADMIN -> {

            }
            Role.STUDENT -> {

            }
            Role.TEACHER -> {

            }
            Role.UNKNOWN -> {

            }
        }
    }

    authGraph(
        navController = navController,
        onShowSnackBar = onShowSnackBar,
    )

}

