package com.rollinup.rollinup.navigation

import SnackBarHost
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Role
import com.rollinup.rollinup.component.bottombar.BottomBar
import com.rollinup.rollinup.component.bottombar.rememberBottomBarState
import com.rollinup.rollinup.screen.main.navigation.MainRoute
import kotlinx.coroutines.launch

@Composable
actual fun AppNavHost(
    navController: NavHostController,
    initialRoute: String,
    loginData: LoginEntity?,
    onRefreshSetting: () -> Unit,
    onLogout: () -> Unit,
) {
    var isSuccess: Boolean? by remember { mutableStateOf(null) }
    var bottomBarHeight by remember { mutableStateOf(0.dp) }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val role = loginData?.role ?: Role.UNKNOWN
    val listMenu = listOf(MainRoute.ProfileRoute, MainRoute.DashBoardRoute, MainRoute.SettingRoute)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val showBottomBar = showBottomBar(navBackStackEntry)

    fun onNavigateTo(route: String) {
        navController
            .navigate(route) {
                popUpTo(currentRoute) {
                    inclusive = true
                }
            }
    }

    fun showSnackBar(msg: String) {
        scope.launch {
            isSuccess?.let {
                snackBarHostState.showSnackbar(message = msg)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackBarHost(
                snackBarHostState = snackBarHostState,
                isSuccess = isSuccess ?: false
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier.padding(bottom = if (showBottomBar) bottomBarHeight else 0.dp)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = initialRoute
                ) {
                    appGraph(
                        navController = navController,
                        onRefreshSetting = onRefreshSetting,
                        onShowSnackBar = { msg, success ->
                            isSuccess = success
                            showSnackBar(msg)
                        }
                    )
                }
            }
            BottomBar(
                listMenu = listMenu,
                onNavigate = { menu ->
                    onNavigateTo(menu.withRole(role))
                },
                state = rememberBottomBarState(initialMenu = MainRoute.DashBoardRoute),
                onGetHeight = { height -> bottomBarHeight = height + 16.dp },
                showBottomBar = showBottomBar,
                onRefresh = {}
            )

        }
    }
}

private fun showBottomBar(
    navBackStackEntry: NavBackStackEntry?,
) = (navBackStackEntry?.destination?.route ?: "") in MainRoute.getRouteWithBottomBar()
