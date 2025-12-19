package com.rollinup.rollinup.screen.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.rollinup.apiservice.model.common.Role
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.platformwarning.PlatformWarning
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.dashboard.ui.screen.studentdashboard.view.StudentDashboardScreen
import com.rollinup.rollinup.screen.dashboard.ui.screen.teacherdashboard.view.TeacherDashboardScreen
import com.rollinup.rollinup.screen.main.screen.attendance.ui.navigation.AttendanceRoute
import com.rollinup.rollinup.screen.main.screen.attendance.ui.navigation.attendanceGraph
import com.rollinup.rollinup.screen.main.screen.globalsetting.ui.screen.view.GlobalSettingScreen
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.view.StudentPermitScreen
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view.TeacherPermitScreen
import com.rollinup.rollinup.screen.main.screen.profile.ui.screen.view.ProfileScreen
import com.rollinup.rollinup.screen.main.screen.setting.ui.view.SettingScreen
import com.rollinup.rollinup.screen.main.screen.studentcenter.navigation.StudentCenterRoute
import com.rollinup.rollinup.screen.main.screen.studentcenter.navigation.studentCenterGraph
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.view.UserCenterScreen

fun NavGraphBuilder.mainGraph(
    onNavigateTo: (String) -> Unit,
    onShowSnackBar: (String, Boolean) -> Unit,
    onNavigateUp: () -> Unit,
    onRefreshSetting:()->Unit,
) {
    composable(
        route = MainRoute.DashBoardRoute.route,
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
                UserCenterScreen(onShowSnackBar)
            }

            Role.STUDENT -> {
                val platform = getPlatform()
                if (platform.isMobile()) {
                    StudentDashboardScreen(
                        onShowSnackBar = onShowSnackBar,
                        onNavigateTo = onNavigateTo,
                        onRefreshSetting = onRefreshSetting
                    )
                } else {
                    PlatformWarning(Role.STUDENT, platform)
                }
            }

            Role.TEACHER -> {
                TeacherDashboardScreen(
                    onShowSnackBar = onShowSnackBar,
                    onNavigateTo = onNavigateTo
                )
            }

            Role.UNKNOWN -> {
                Scaffold { }
            }
        }
    }

    composable(
        route = MainRoute.ProfileRoute.route,
        arguments = listOf(
            navArgument(
                name = "role",
                builder = {
                    type = NavType.StringType
                }
            )
        )
    ) {
        ProfileScreen(onShowSnackBar)
    }

    composable(
        route = MainRoute.SettingRoute.route,
    ) {
        SettingScreen()
    }

    composable(
        route = MainRoute.PermitRoute.route,
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
            Role.STUDENT -> {
                StudentPermitScreen { onNavigateUp() }
            }

            Role.ADMIN -> {
                Scaffold { }
            }

            Role.TEACHER -> {
                TeacherPermitScreen(
                    onNavigateUp = onNavigateUp,
                    onShowSnackBar = onShowSnackBar
                )
            }

            Role.UNKNOWN -> {}
        }
    }

    composable(
        route = MainRoute.GlobalSettingRoute.route
    ) {
        GlobalSettingScreen()
    }

    navigation(
        route = MainRoute.AttendanceRoute.route,
        startDestination = AttendanceRoute.AttendanceHomeRoute.route
    ) {
        attendanceGraph(
            onNavigateTo = onNavigateTo,
            onNavigateUp = onNavigateUp,
            onShowSnackBar = onShowSnackBar
        )
    }

    navigation(
        route = MainRoute.StudentCenterRoute.route,
        startDestination = StudentCenterRoute.StudentCenterHomeRoute.route
    ) {
        studentCenterGraph(
            onNavigateTo = onNavigateTo,
            onNavigateUp = onNavigateUp,
            onShowSnackBar = onShowSnackBar
        )
    }

}