@file:OptIn(ExperimentalMaterial3Api::class)

package com.rollinup.rollinup.screen.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.model.auth.LoginEntity
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.ActionButton
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.button.ButtonType
import com.rollinup.rollinup.component.camera.CameraView
import com.rollinup.rollinup.component.card.Card
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.filepicker.FileHandler
import com.rollinup.rollinup.component.filepicker.FilePicker
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.model.Severity
import com.rollinup.rollinup.component.pullrefresh.PullRefresh
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.generalSetting
import com.rollinup.rollinup.component.theme.localUser
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.topbar.TopBar
import com.rollinup.rollinup.component.utils.toAnnotatedString
import com.rollinup.rollinup.navigation.NavigationRoute
import com.rollinup.rollinup.screen.auth.navigation.AuthNavigationRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_camera_line_24
import rollin_up.composeapp.generated.resources.ic_check_line_24
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.icon_search_line_24

@Composable
fun TestScreen(
    onShowSnackBar: (String, Boolean) -> Unit,
    onNavigateUp: () -> Unit,
    navController: NavController,
    onUpdateLoginData: (LoginEntity?) -> Unit,
) {
    var searchValue by remember { mutableStateOf("") }
    var isShowCamera by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TestTopAppBar(
                onSearch = { value ->
                    searchValue = value
                },
                onNavigateUp = onNavigateUp,
                onLogout = {
                    onUpdateLoginData(null)
                    navController.navigate(AuthNavigationRoute.Login.route) {
                        popUpTo(NavigationRoute.TestRoute.route) {
                            inclusive = true
                        }
                    }
                },
                onOpenCamera = { isShowCamera = true }
            )
        }
    ) {

        CameraView(
            onDismissRequest = { isShowCamera = it },
            onCapture = {},
            onError = {},
            notification = {
                Chip(
                    text = "Make sure your face can be identified easily",
                    severity = Severity.SECONDARY,
                    textStyle = Style.body
                )
            },
            isShowCamera = isShowCamera,
            successMsg = "Photo captured",
            errorMsg = "Failed to take photo",
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var showShimmer by remember { mutableStateOf(false) }
            val user = localUser
            val scope = rememberCoroutineScope()
            var selected by remember { mutableStateOf(listOf<Int>()) }
            var isRefreshing by remember { mutableStateOf(false) }

            LaunchedEffect(showShimmer) {
                delay(1000)
                if (showShimmer) showShimmer = false
            }
            Chip(
                text = "Test",
                toolTipContent = "Test is fallen",
                leadingIcon = Res.drawable.icon_search_line_24,
                onClickLeadIcon = {
                    L.wtf { "Chip icon clicked" }
                }
            )
            Button(
                text = "Start Shimmer",
                type = ButtonType.TEXT,
                onClick = {
                    showShimmer = true
                },
                severity = Severity.PRIMARY
            )

            FilePicker()

            Spacer(itemGap8)
            ShimmerEffect(120.dp)
            Spacer(itemGap8)
            Text(
                text = user.toString(),
                style = Style.header,
                color = theme.bodyText
            )
            Spacer(itemGap8)
            Text(
                text = generalSetting.toString(),
                style = Style.body,
                color = theme.bodyText,
                modifier = Modifier.padding(screenPadding)
            )
            if (isRefreshing) {
                Text(
                    text = "Refreshing",
                    style = Style.body,
                    color = theme.bodyText,
                    modifier = Modifier.padding(screenPadding)
                )
            }
            PullRefresh(
                isRefreshing = isRefreshing,
                onRefresh = {
                    scope.launch {
                        isRefreshing = true
                        delay(1000)
                        isRefreshing = false
                    }
                }
            ) {
                LazyColumn(
                    contentPadding = screenPadding,
                    verticalArrangement = Arrangement.spacedBy(itemGap4)
                ) {
                    repeat(10) {
                        item {
                            Card(
                                onClick = {
                                    if (selected.isNotEmpty()) {
                                        if (1 in selected) {
                                            selected = selected.filter { it != 1 }
                                        } else {
                                            selected = selected.plus(1)
                                        }
                                    }
                                },
                                onLongClick = {
                                    if (selected.isEmpty()) {
                                        selected = selected.plus(1)
                                    }
                                },
                                backgroundColor = if (1 in selected) theme.popUpBgSelected else theme.popUpBg
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(itemGap4)
                                ) {
                                    Text(
                                        text = "Title",
                                        style = Style.title,
                                        color = theme.bodyText
                                    )
                                    Text(
                                        text = "Body",
                                        style = Style.body,
                                        color = theme.bodyText
                                    )
                                }
                            }
                        }
                        item {
                            Card(
                                onClick = {
                                    if (selected.isNotEmpty()) {
                                        if (2 in selected) {
                                            selected = selected.filter { it != 2 }
                                        } else {
                                            selected = selected.plus(2)
                                        }
                                    }
                                },
                                onLongClick = {
                                    if (selected.isEmpty()) {
                                        selected = selected.plus(2)
                                    }
                                },
                                backgroundColor = if (2 in selected) theme.popUpBgSelected else theme.popUpBg,
                                showAction = selected.isEmpty(),
                                onClickAction = {}
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(itemGap4)
                                ) {
                                    Text(
                                        text = "Title",
                                        style = Style.title,
                                        color = theme.bodyText
                                    )
                                    Text(
                                        text = "Body",
                                        style = Style.body,
                                        color = theme.bodyText
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TestTopAppBar(
    onSearch: (String) -> Unit,
    onNavigateUp: () -> Unit,
    onLogout: () -> Unit,
    onOpenCamera: () -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var showBottomSheet2 by remember { mutableStateOf(false) }

    val listMenu = listOf(
        Menu.SEARCH,
        Menu.FILTER,
        Menu.PRINT,
    )

    TopBar(
        onClickMenu = { menu ->
            when (menu) {
                Menu.FILTER -> {
                    showBottomSheet = true
                }

                Menu.PRINT -> {
                    showBottomSheet2 = true
                }

                else -> {}
            }
        },
        menu = listMenu,
        onSearch = onSearch,
        onNavigateUp = onNavigateUp,
        title = "Test Screen",
    )

    BottomSheetWithButton(
        onClickConfirm = {},
        isShowSheet = showBottomSheet2,
        onDismissRequest = { showBottomSheet2 = it },
        onOpenCamera = onOpenCamera
    )
}

@Composable
fun BottomSheetWithButton(
    onClickConfirm: () -> Unit,
    isShowSheet: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onOpenCamera: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var showAnnotatedDialog by remember { mutableStateOf(false) }


    BottomSheet(
        onClickConfirm = {
            onClickConfirm()
        },
        onDismissRequest = onDismissRequest,
        isShowSheet = isShowSheet,
    ) {
        ActionButton(
            label = "Show Text Dialog",
            onClick = {
                showDialog = true
                onDismissRequest(false)
            }
        )
        ActionButton(
            label = "Show Annotated Text",
            onClick = {
                showAnnotatedDialog = true
                onDismissRequest(false)
            },
            icon = Res.drawable.ic_check_line_24,
            iconTint = theme.danger
        )
        ActionButton(
            label = "Open Camera",
            onClick = {
                onOpenCamera()
                onDismissRequest(false)
            },
            icon = Res.drawable.ic_camera_line_24,
            iconTint = theme.primary
        )
    }

    AlertDialog(
        isShowDialog = showDialog,
        onDismissRequest = { showDialog = it },
        content = "My name is Yoshikage Kira. I'm 33 years old. My house is in the northeast section of Morioh, where all the villas are, and I am not married.",
        onClickConfirm = {},
        title = "Hello, This is Kira",
        icon = Res.drawable.ic_info_line_24,
        iconTint = theme.primary,
        onClickCancel = {}
    )

    AlertDialog(
        isShowDialog = showAnnotatedDialog,
        onDismissRequest = { showAnnotatedDialog = it },
        content = "**123456** **789** 012**34 3**567890 **1234567890**".toAnnotatedString(),
        isSingleButton = true,
        showCancelButton = true,
        onClickConfirm = {},
        onClickCancel = {}
    )
}
