package com.rollinup.rollinup.navigation

import com.rollinup.apiservice.model.common.Role
import com.rollinup.rollinup.screen.auth.navigation.AuthNavigationRoute

sealed class NavigationRoute(val route: String) {
    object SplashScreen : NavigationRoute("splash-screen")
    object Auth : NavigationRoute("auth")
    object MainRoute : NavigationRoute("main/{role}") {
        fun navigate(role: Role): String {
            return route.replace("{role}", role.value)
        }
    }

    companion object {
        fun getRouteWithoutRail() = listOf<String>(
            Auth.route,
            SplashScreen.route,
            AuthNavigationRoute.Login.route,
            AuthNavigationRoute.ResetPassword.route,
            AuthNavigationRoute.UpdatePassword.route
        )
    }
}


