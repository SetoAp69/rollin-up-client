package com.rollinup.rollinup.screen.splashscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.Utils
import com.rollinup.rollinup.component.utils.getVersion
import com.rollinup.rollinup.screen.splashscreen.viewmodel.SplashScreenViewmodel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_launcher_logo_foreground_24

@Composable
fun SplashScreen(
    onLogin: (LoginEntity) -> Unit,
    onGoToLogin: () -> Unit,
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
                    onLogin(loginData)
                }

                false -> {
                    onGoToLogin()
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
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .size(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_launcher_logo_foreground_24),
                        contentDescription = "App Logo",
                        tint = theme.primary,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
            Box(
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    text = getVersion(),
                    style = Style.body,
                    color = theme.chipSecondaryBg
                )
            }
        }
    }
}