package com.rollinup.rollinup.screen.main.screen.studentcenter.navigation

sealed class StudentCenterRoute(val route: String) {
    object StudentCenterHomeRoute : StudentCenterRoute("student-center")
    object StudentProfileRoute : StudentCenterRoute("student-center/{studentId}") {
        fun navigate(id: String): String {
            return this.route.replace("{studentId}", id)
        }
    }
}