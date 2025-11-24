package com.rollinup.rollinup.navigation

//import com.rollinup.rollinup.component.theme.localUserData
import SnackBarHost
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Role
import com.rollinup.rollinup.AuthUiState
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.navigationrail.NavigationRail
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import com.rollinup.rollinup.component.theme.localUser
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.navigation.NavigationRoute.Companion.showsRail
import com.rollinup.rollinup.screen.main.navigation.MainRoute
import kotlinx.coroutines.launch


@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var isSuccess by remember { mutableStateOf(false) }

    val loginState = LocalAuthViewmodel.current.uiState.value.loginState
    val loginData = localUser
    val authViewmodel = LocalAuthViewmodel.current

    if (getPlatform().isMobile()) {
        MobileNavigationHost(
            navController = navController,
            loginData = loginData,
            initialRoute = getInitialRoute(loginState, loginData),
            snackBarHost = {
                SnackBarHost(
                    snackBarHostState = snackBarHostState,
                    isSuccess = isSuccess
                )
            },
            onShowSnackBar = { text, success ->
                coroutineScope.launch {
                    isSuccess = success
                    snackBarHostState.showSnackBar(message = text, isSuccess = success)
                }
            }
        )
    } else {
        DesktopNavigationHost(
            navController = navController,
            loginData = loginData,
            initialRoute = getInitialRoute(loginState, loginData),
            snackBarHost = {
                SnackBarHost(
                    snackBarHostState = snackBarHostState,
                    isSuccess = isSuccess
                )
            },
            onShowSnackBar = { text, success ->
                coroutineScope.launch {
                    isSuccess = success
                    snackBarHostState.showSnackBar(message = text, isSuccess = success)
                }
            },
            onLogOut = {
                authViewmodel.logout()
                navController.navigate(NavigationRoute.Auth.route) {
                    popUpTo(NavigationRoute.MainRoute.route) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

@Composable
fun MobileNavigationHost(
    navController: NavHostController,
    loginData: LoginEntity?,
    initialRoute: String,
    snackBarHost: @Composable () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    Scaffold(
        snackbarHost = { snackBarHost() },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            loginData = loginData,
            initialRoute = initialRoute,
            onShowSnackBar = onShowSnackBar
        )
    }
}

@Composable
fun NavHost(
    navController: NavHostController,
    loginData: LoginEntity?,
    initialRoute: String,
    onShowSnackBar: OnShowSnackBar,
) {
    NavHost(
        navController = navController,
        startDestination = initialRoute
//        startDestination = NavigationRoute.MainRoute.navigate(Role.TEACHER)
    ) {
        appGraph(
            navController = navController,
            onShowSnackBar = onShowSnackBar,
        )
    }
}

@Composable
fun DesktopNavigationHost(
    snackBarHost: @Composable () -> Unit,
    navController: NavHostController,
    loginData: LoginEntity?,
    initialRoute: String,
    onLogOut: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route ?: ""
    val role = loginData?.role ?: Role.TEACHER
    LaunchedEffect(currentRoute){
        L.wtf{currentRoute}
    }

    com.rollinup.rollinup.component.scaffold.Scaffold(
        showNavigation = navController.currentBackStackEntry?.destination?.route?.showsRail()?:false,
        snackBarHost = snackBarHost,
        navigationRail = {
            NavigationRail(
                menu = MainRoute.getRoute(role),
                isExpandable = true,
                onClickMenu = { menu ->
                    navController.navigate(menu.withRole(role))
                },
            )
        },
        topBar = {
            NavHostTopBar(
                userData = loginData ?: LoginEntity(),
                onLogOut = onLogOut
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = initialRoute
        ) {
            appGraph(
                navController = navController,
                onShowSnackBar = onShowSnackBar,
            )
        }
    }
}


private fun getInitialRoute(loginState: AuthUiState.LoginState?, loginData: LoginEntity?): String {
    return when (loginState) {
        AuthUiState.LoginState.Login -> NavigationRoute.MainRoute.navigate(loginData?.role!!)
        AuthUiState.LoginState.Logout -> NavigationRoute.Auth.route
        null -> {
            NavigationRoute.SplashScreen.route
        }
    }
}

private suspend fun SnackbarHostState.showSnackBar(
    message: String,
    isSuccess: Boolean,
) {
    this.showSnackbar(
        message = message,
        actionLabel = null,
        withDismissAction = false,
    )
}