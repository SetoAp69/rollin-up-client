package com.rollinup.rollinup.component.scaffold

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rollinup.rollinup.component.loading.LoadingOverlay

@Composable
fun Scaffold(
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    fab: @Composable () -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    fabPosition: FabPosition = FabPosition.End,
    showLoadingOverlay: Boolean = false,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackBarHost,
        floatingActionButton = fab,
        floatingActionButtonPosition = fabPosition,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            content()
        }
        LoadingOverlay(show = showLoadingOverlay)
    }
}

@Composable
fun Scaffold(
    topBar: @Composable () -> Unit = {},
    navigationRail: @Composable () -> Unit,
    showNavigation: Boolean = true,
    fabPosition: FabPosition = FabPosition.End,
    fab: @Composable () -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    showLoadingOverlay: Boolean = false,
    content: @Composable () -> Unit,
) {
    Scaffold(
        snackbarHost = snackBarHost,
        floatingActionButton = fab,
        floatingActionButtonPosition = fabPosition,
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (showNavigation) {
                AnimatedVisibility(
                    visible = showNavigation,
                    enter = slideInHorizontally(initialOffsetX = { it }),
                    exit = slideOutHorizontally(targetOffsetX = { it }),
                ) { navigationRail() }
            }
            Column(modifier = Modifier.padding(paddingValues)) {
                if (showNavigation) {
                    topBar()
                }
                content()
            }
        }
        LoadingOverlay(show = showLoadingOverlay)
    }
}