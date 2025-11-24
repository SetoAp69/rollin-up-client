package com.rollinup.rollinup.screen.splashscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.rollinup.navigation.NavigationRoute
import com.rollinup.rollinup.screen.auth.navigation.AuthNavigationRoute
import com.rollinup.rollinup.screen.splashscreen.viewmodel.SplashScreenViewmodel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    onUpdateLoginData: (LoginEntity) -> Unit,
) {
    val isDark = isSystemInDarkTheme()
    val primary = if (isDark) Color(0xFF704DC8) else Color(0xFF965FD4)
    val viewModel: SplashScreenViewmodel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    DisposableEffect(uiState.loginState) {
        uiState.loginState?.let { state ->
            when (state) {
                true -> {
                    val loginData = uiState.loginData!!
                    onUpdateLoginData(loginData)
                    navController.navigate(NavigationRoute.MainRoute.navigate(loginData.role)) {
                        popUpTo(NavigationRoute.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                }

                false -> {
                    navController.navigate(AuthNavigationRoute.Login.route) {
                        popUpTo(NavigationRoute.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
        onDispose {
            viewModel.resetState()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.auth()
    }

    Scaffold { it ->
        Box(
            modifier = Modifier
                .background(primary)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(100.dp)
            ) {

            }
        }
    }
}