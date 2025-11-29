package com.rollinup.rollinup.screen.main.screen.attendance.ui.navigation

sealed class AttendanceRoute(val route: String) {
    object AttendanceHomeRoute : AttendanceRoute("attendance")
    object AttendanceByStudentRoute : AttendanceRoute("attendance/{studentId}") {
        fun navigate(studentId: String) = "attendance/$studentId"
    }
}