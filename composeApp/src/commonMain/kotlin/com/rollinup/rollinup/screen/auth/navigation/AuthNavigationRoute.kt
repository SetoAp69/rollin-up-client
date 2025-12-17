package com.rollinup.rollinup.screen.auth.navigation

sealed class AuthNavigationRoute(val route: String) {
    object Login : AuthNavigationRoute("auth/login")
    object ResetPassword : AuthNavigationRoute("auth/reset-password")
    object UpdatePassword : AuthNavigationRoute("auth/update-password")
}