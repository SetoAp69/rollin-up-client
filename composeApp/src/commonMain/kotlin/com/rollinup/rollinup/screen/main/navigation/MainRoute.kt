package com.rollinup.rollinup.screen.main.navigation

import com.rollinup.apiservice.model.common.Role
import com.rollinup.rollinup.component.navigationrail.NavigationMenu
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_gear_fill_24
import rollin_up.composeapp.generated.resources.ic_gear_line_24
import rollin_up.composeapp.generated.resources.ic_home_fill_24
import rollin_up.composeapp.generated.resources.ic_home_line_24
import rollin_up.composeapp.generated.resources.ic_mail_open_line_24
import rollin_up.composeapp.generated.resources.ic_user_board_line_24
import rollin_up.composeapp.generated.resources.ic_user_check_line_24
import rollin_up.composeapp.generated.resources.ic_user_filled_24
import rollin_up.composeapp.generated.resources.ic_user_line_24
import rollin_up.composeapp.generated.resources.label_attendance
import rollin_up.composeapp.generated.resources.label_dashboard
import rollin_up.composeapp.generated.resources.label_global_setting
import rollin_up.composeapp.generated.resources.label_permit
import rollin_up.composeapp.generated.resources.label_profile
import rollin_up.composeapp.generated.resources.label_setting
import rollin_up.composeapp.generated.resources.label_student_center

sealed class MainRoute(
    route: String,
    icon: DrawableResource,
    title: StringResource,
    filledIcon: DrawableResource = icon,
) : NavigationMenu(route, icon, title, filledIcon) {
    object DashBoardRoute : MainRoute(
        route = "main/dashboard/{role}",
        icon = Res.drawable.ic_home_line_24,
        title = Res.string.label_dashboard,
        filledIcon = Res.drawable.ic_home_fill_24
    )

    object SettingRoute : MainRoute(
        route = "main/setting/",
        icon = Res.drawable.ic_gear_line_24,
        title = Res.string.label_setting,
        filledIcon = Res.drawable.ic_gear_fill_24
    )

    object GlobalSettingRoute : MainRoute(
        route = "main/global-setting/",
        icon = Res.drawable.ic_gear_line_24,
        title = Res.string.label_global_setting
    )

    object ProfileRoute : MainRoute(
        route = "main/profile/{role}",
        icon = Res.drawable.ic_user_line_24,
        title = Res.string.label_profile,
        filledIcon = Res.drawable.ic_user_filled_24
    )

    object StudentCenterRoute : MainRoute(
        route = "main/student-center/",
        icon = Res.drawable.ic_user_board_line_24,
        title = Res.string.label_student_center
    )

    object PermitRoute : MainRoute(
        route = "main/permit/{role}",
        icon = Res.drawable.ic_mail_open_line_24,
        title = Res.string.label_permit
    )

    object AttendanceRoute : MainRoute(
        route = "main/attendance/",
        icon = Res.drawable.ic_user_check_line_24,
        title = Res.string.label_attendance
    )

    companion object {
        fun getRoute(role: Role): List<MainRoute> =
            when (role) {
                Role.ADMIN -> listOf(
                    DashBoardRoute,
                    GlobalSettingRoute,
                )

                Role.TEACHER -> listOf(
                    DashBoardRoute, AttendanceRoute, PermitRoute,
                    StudentCenterRoute
                )

                else -> listOf()
            }

        fun getRouteWithBottomBar() = listOf(
            ProfileRoute.route,
            SettingRoute.route,
            DashBoardRoute.route
        )
    }

}