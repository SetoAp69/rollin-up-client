package com.rollinup.rollinup.screen.main.screen.attendance.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view.AttendanceByStudentScreen
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view.AttendanceHomeScreen

fun NavGraphBuilder.attendanceGraph(
    onNavigateTo: (String) -> Unit,
    onNavigateUp: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    composable(AttendanceRoute.AttendanceHomeRoute.route) {
        AttendanceHomeScreen(
            onNavigateUp = onNavigateUp,
            onNavigateTo = onNavigateTo,
            onShowSnackBar = onShowSnackBar
        )
    }
    composable(
        route = AttendanceRoute.AttendanceByStudentRoute.route,
        arguments = listOf(
            navArgument(
                name = "studentId",
                builder = {
                    NavType.StringType
                }
            )
        )
    ) { backStackEntry ->
        val id = backStackEntry.savedStateHandle.get<String>("studentId") ?: ""
        AttendanceByStudentScreen(
            id = id,
            onNavigateUp = onNavigateUp,
            onShowSnackBar = onShowSnackBar
        )

    }
}