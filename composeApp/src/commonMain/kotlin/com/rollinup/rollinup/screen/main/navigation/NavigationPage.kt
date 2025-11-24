package com.rollinup.rollinup.screen.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rollinup.apiservice.model.common.Role
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.platformwarning.PlatformWarning
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.dashboard.ui.screen.studentdashboard.view.StudentDashboardScreen
import com.rollinup.rollinup.screen.dashboard.ui.screen.teacherdashboard.view.TeacherDashboardScreen

fun NavGraphBuilder.mainGraph(
    onNavigateTo: (String) -> Unit,
    onShowSnackBar: (String, Boolean) -> Unit,
    onNavigateUp: () -> Unit,
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
                StudentDashboardScreen(
                    onShowSnackBar = onShowSnackBar,
                    onNavigateTo = onNavigateTo
                )
            }

            Role.STUDENT -> {
                val platform = getPlatform()
                if (platform.isMobile()) {
                    StudentDashboardScreen(
                        onShowSnackBar = onShowSnackBar,
                        onNavigateTo = onNavigateTo
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
        Scaffold { }
    }

    composable(
        route = MainRoute.SettingRoute.route,
        arguments = listOf(
            navArgument(
                name = "role",
                builder = {
                    type = NavType.StringType
                }
            )
        )
    ) {
        Scaffold {}
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

        Scaffold { }
    }

    composable(
        route = MainRoute.AttendanceRoute.route,
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

        Scaffold { }
    }

    composable(
        route = MainRoute.StudentCenterRoute.route,
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

        Scaffold { }
    }

}