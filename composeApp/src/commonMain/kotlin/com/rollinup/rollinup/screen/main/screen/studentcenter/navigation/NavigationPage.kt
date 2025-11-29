package com.rollinup.rollinup.screen.main.screen.studentcenter.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.view.StudentCenterScreen
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentdetail.view.StudentProfileScreen

fun NavGraphBuilder.studentCenterGraph(
    onNavigateTo: (String) -> Unit,
    onNavigateUp: () -> Unit,
) {
    composable(route = StudentCenterRoute.StudentCenterHomeRoute.route) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(theme.success)
//        ) {  }
        StudentCenterScreen(
            onNavigateUp = onNavigateUp,
            onNavigateTo = onNavigateTo
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
            id = navBackStackEntry.savedStateHandle.get<String>("id").toString(),
            onNavigateUp = onNavigateUp
        )
    }
}