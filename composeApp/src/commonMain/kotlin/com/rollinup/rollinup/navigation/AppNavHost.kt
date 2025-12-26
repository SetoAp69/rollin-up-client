package com.rollinup.rollinup.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.rollinup.apiservice.model.auth.LoginEntity

@Composable
expect fun AppNavHost(
    navController: NavHostController,
    initialRoute:String,
    loginData: LoginEntity?,
    onRefreshSetting:()->Unit,
    onLogout:()->Unit,
)