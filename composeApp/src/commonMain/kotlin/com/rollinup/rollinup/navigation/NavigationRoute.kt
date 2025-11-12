package com.rollinup.rollinup.navigation

import com.rollinup.apiservice.model.common.Role

sealed class NavigationRoute(val route: String) {
    //    object Initial : NavigationRoute("") {
//        fun navigate(loginData: LoginEntity?): String {
//            return if (loginData == null) {
//                Auth.route
//            } else {
//                TestRoute.route
//            }
//        }
//    }
    object SplashScreen : NavigationRoute("splash-screen")

    object TestRoute : NavigationRoute("test")
    object Dashboard : NavigationRoute("dashboard/{role}") {
        fun navigate(role: Role): String {
            return Dashboard.route.replace("{role}", role.value)
        }
    }

    object Auth : NavigationRoute("auth")
}


