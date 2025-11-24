package com.rollinup.rollinup.navigation

import com.rollinup.apiservice.model.common.Role

sealed class NavigationRoute(val route: String) {
    object SplashScreen : NavigationRoute("splash-screen")
    object Auth : NavigationRoute("auth")
    object TestRoute : NavigationRoute("test")
    object MainRoute : NavigationRoute("main/{role}") {
        fun navigate(role: Role): String {
            return MainRoute.route.replace("{role}", role.value)
        }
    }

    companion object {
        fun String.showsRail(): Boolean {
            return this !in listOf(
                SplashScreen.route,
                Auth.route,
                "auth/reset-password",
                "auth/login"
            )
        }
    }
}


