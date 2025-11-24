package com.rollinup.rollinup.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.rollinup.apiservice.model.common.Role
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.platformwarning.PlatformWarning
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.auth.navigation.AuthNavigationRoute
import com.rollinup.rollinup.screen.auth.navigation.authGraph
import com.rollinup.rollinup.screen.auth.ui.screen.loginscreen.view.LoginScreen
import com.rollinup.rollinup.screen.dashboard.ui.screen.studentdashboard.view.StudentDashboardScreen
import com.rollinup.rollinup.screen.main.navigation.MainRoute
import com.rollinup.rollinup.screen.main.navigation.mainGraph
import com.rollinup.rollinup.screen.splashscreen.SplashScreen
import com.rollinup.rollinup.screen.test.ui.view.TestScreen


fun NavGraphBuilder.appGraph(
    navController: NavHostController,
    onShowSnackBar: (String, Boolean) -> Unit,
) {

    val onNavigateUp: () -> Unit = { navController.popBackStack() }
    val onNavigateTo: (String) -> Unit = { navController.navigate(it) }

    composable(NavigationRoute.SplashScreen.route) {
        val authViewModel = LocalAuthViewmodel.current
        SplashScreen(navController = navController) { loginData ->
            authViewModel.login(loginData)
        }
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
            onNavigateTo = onNavigateTo,
            onShowSnackBar = onShowSnackBar,
            onNavigateUp = onNavigateUp
        )
    }


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

    authGraph(
        navController = navController,
        onShowSnackBar = onShowSnackBar,
    )

}

