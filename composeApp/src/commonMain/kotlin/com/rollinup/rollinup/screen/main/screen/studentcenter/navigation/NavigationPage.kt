package com.rollinup.rollinup.screen.main.screen.studentcenter.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.view.StudentCenterScreen
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentdetail.view.StudentProfileScreen

fun NavGraphBuilder.studentCenterGraph(
    onNavigateTo: (String) -> Unit,
    onNavigateUp: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    composable(route = StudentCenterRoute.StudentCenterHomeRoute.route) {
        StudentCenterScreen(
            onNavigateUp = onNavigateUp,
            onNavigateTo = onNavigateTo,
            onShowSnackBar = onShowSnackBar
        )
    }

    composable(
        route = StudentCenterRoute.StudentProfileRoute.route,
        arguments = listOf(
            navArgument(
                name = "studentId",
                builder = {
                    type = NavType.StringType
                }
            )
        )
    ) { navBackStackEntry ->
        StudentProfileScreen(
            id = navBackStackEntry.savedStateHandle.get<String>("studentId").toString(),
            onNavigateUp = onNavigateUp,
            onShowSnackBar = onShowSnackBar
        )
    }
}