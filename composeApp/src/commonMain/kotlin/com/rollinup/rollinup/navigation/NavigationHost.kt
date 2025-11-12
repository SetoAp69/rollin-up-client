package com.rollinup.rollinup.navigation

//import com.rollinup.rollinup.component.theme.localUserData
import SnackBarHost
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.rollinup.rollinup.AuthUiState
import com.rollinup.rollinup.component.theme.LocalAuthViewmodel
import kotlinx.coroutines.launch


@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var isSuccess by remember { mutableStateOf(false) }

    val loginState = LocalAuthViewmodel.current.uiState.value.loginState

    Scaffold(
        snackbarHost = {
            SnackBarHost(
                snackBarHostState = snackBarHostState,
                isSuccess = isSuccess
            )
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
//             startDestination = getInitialRoute(loginState)
             startDestination = NavigationRoute.TestRoute.route
        ) {
            mainGraph(
                navController = navController,
                onShowSnackBar = { text, success ->
                    coroutineScope.launch {
                        isSuccess = success
                        snackBarHostState.showSnackBar(message = text, isSuccess = success)
                    }
                },
            )
        }
    }

}

private fun getInitialRoute(loginState: AuthUiState.LoginState?):String {
    return when (loginState) {
        AuthUiState.LoginState.Login -> NavigationRoute.TestRoute.route
        AuthUiState.LoginState.Logout -> NavigationRoute.Auth.route
        null->{
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
