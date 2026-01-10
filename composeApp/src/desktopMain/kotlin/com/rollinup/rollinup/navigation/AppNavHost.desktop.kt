package com.rollinup.rollinup.navigation

import SnackBarHost
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.apiservice.model.common.Role
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.button.IconButton
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.dropdown.DropDownMenuItem
import com.rollinup.rollinup.component.navigationrail.NavigationRail
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.LocalUiModeViewModel
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.topbar.BaseTopBar
import com.rollinup.rollinup.component.topbar.TopAppBarDefaults
import com.rollinup.rollinup.screen.main.navigation.MainRoute
import com.rollinup.rollinup.screen.main.screen.setting.ui.view.UiModeSwitch
import kotlinx.coroutines.launch
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_drop_down_arrow_line_down_24
import rollin_up.composeapp.generated.resources.ic_exit_line_24

@Composable
actual fun AppNavHost(
    navController: NavHostController,
    initialRoute: String,
    loginData: LoginEntity?,
    onRefreshSetting: () -> Unit,
    onLogout: () -> Unit,
    onFinish: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var isSuccess: Boolean? by remember { mutableStateOf(null) }

    val role = loginData?.role ?: Role.UNKNOWN
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    fun showSnackBar(msg: String) {
        scope.launch {
            isSuccess?.let {
                snackBarHostState.showSnackbar(message = msg)
            }
        }
    }

    Scaffold(
        topBar = {
            NavHostTopBar(
                userData = loginData ?: LoginEntity(),
                onLogOut = onLogout
            )
        },
        showNavigation = showNavRail(navBackStackEntry),
        navigationRail = {
            NavigationRail(
                menu = MainRoute.getRoute(role),
                isExpandable = true,
                onClickMenu = { menu ->
                    navController.navigate(
                        route = menu.withRole(role),
                    )
                },
            )
        },
        snackBarHost = {
            SnackBarHost(
                snackBarHostState = snackBarHostState,
                isSuccess = isSuccess ?: false
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = initialRoute
        ) {
            appGraph(
                onShowSnackBar = { msg, success ->
                    isSuccess = success
                    showSnackBar(msg)
                },
                navController = navController,
                onRefreshSetting = {},
                onFinish = onFinish
            )
        }
    }
}


@Composable
fun NavHostTopBar(
    userData: LoginEntity,
    onLogOut: () -> Unit,
) {
    val title =
        if (userData.role == Role.ADMIN) "Rollin up - Admin Console" else "Rollin up - Client"
    BaseTopBar(
        title = title,
        showNavigateUp = false,
        color = TopAppBarDefaults.topAppBarColors.copy(containerColor = theme.popUpBg),
        actionContent = {
            NavHostTopBarContent(
                userData = userData,
                onLogOut = onLogOut
            )
        },
    )
}

@Composable
private fun NavHostTopBarContent(
    userData: LoginEntity,
    onLogOut: () -> Unit,
) {
    var showDropDown by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "Hello",
                color = theme.bodyText,
                style = Style.body
            )
            Spacer(2.dp)
            Text(
                text = userData.fullName,
                color = theme.bodyText,
                style = Style.popupTitle
            )
        }
        Spacer(itemGap4)
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = theme.primary,
                    shape = RoundedCornerShape(50)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userData.firstName.take(1).uppercase().ifBlank { "-" },
                style = Style.title,
                color = Color.White
            )
        }
        Spacer(itemGap4)
        ActionDropDown(
            showDropDown = showDropDown,
            onToggleDropDown = { showDropDown = it },
            onLogOut = onLogOut
        )
    }
}

@Composable
private fun ActionDropDown(
    showDropDown: Boolean,
    onToggleDropDown: (Boolean) -> Unit,
    onLogOut: () -> Unit,
) {
    val uiModeViewModel = LocalUiModeViewModel.current
    val uiMode = uiModeViewModel.uiMode.collectAsStateWithLifecycle().value

    Box {
        IconButton(
            icon = Res.drawable.ic_drop_down_arrow_line_down_24,
            severity = Severity.PRIMARY,
            size = 16.dp,
            onClick = {
                onToggleDropDown(!showDropDown)
            }
        )
        DropDownMenu(
            isShowDropDown = showDropDown,
            onDismissRequest = { onToggleDropDown(false) },
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        vertical = itemGap8,
                        horizontal = 16.dp
                    )
                    .width(150.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                UiModeSwitch(
                    value = uiMode,
                    onValueChanges = { value -> uiModeViewModel.updateUiMode(value) }
                )
            }
            DropDownMenuItem(
                label = "Logout",
                icon = Res.drawable.ic_exit_line_24,
                onClick = onLogOut
            )
        }
    }
}

private fun showNavRail(navBackStackEntry: NavBackStackEntry?) =
    navBackStackEntry?.let {
        it.destination.route !in NavigationRoute.getRouteWithoutRail()
    } ?: false