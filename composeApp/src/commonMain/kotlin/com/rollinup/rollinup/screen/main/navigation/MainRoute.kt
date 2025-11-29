package com.rollinup.rollinup.screen.main.navigation

import com.rollinup.apiservice.model.common.Role
import com.rollinup.rollinup.component.navigationrail.NavigationMenu
import org.jetbrains.compose.resources.DrawableResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_gear_line_24
import rollin_up.composeapp.generated.resources.ic_home_line_24
import rollin_up.composeapp.generated.resources.ic_mail_line_24
import rollin_up.composeapp.generated.resources.ic_mail_open_line_24
import rollin_up.composeapp.generated.resources.ic_user_board_line_24
import rollin_up.composeapp.generated.resources.ic_user_check_line_24
import rollin_up.composeapp.generated.resources.ic_user_line_24

sealed class MainRoute(
    route: String,
    icon: DrawableResource,
    title: String,
) : NavigationMenu(route, icon, title) {
    object DashBoardRoute : MainRoute(
        route = "main/dashboard/{role}",
        icon = Res.drawable.ic_home_line_24,
        title = "Dashboard"
    )

    object SettingRoute : MainRoute(
        route = "main/setting/{role}",
        icon = Res.drawable.ic_gear_line_24,
        title = "Setting"
    )

    object ProfileRoute : MainRoute(
        route = "main/profile/{role}",
        icon = Res.drawable.ic_user_line_24,
        title = "Profile"
    )

    object StudentCenterRoute : MainRoute(
        route = "main/student-center/",
        icon = Res.drawable.ic_user_board_line_24,
        title = "Student Center"
    )

    object PermitRoute : MainRoute(
        route = "main/permit/{role}",
        icon = Res.drawable.ic_mail_open_line_24,
        title = "Permit"
    )

    object AttendanceRoute : MainRoute(
        route = "main/attendance/",
        icon = Res.drawable.ic_user_check_line_24,
        title = "Attendance"
    )

    companion object {
        fun getRoute(role: Role): List<MainRoute> =
            when (role) {
                Role.ADMIN -> listOf(
                    DashBoardRoute,
                    SettingRoute,
                )

                Role.TEACHER -> listOf(
                    DashBoardRoute, AttendanceRoute, PermitRoute,
                    StudentCenterRoute
                )

                else -> listOf()
            }
    }

}