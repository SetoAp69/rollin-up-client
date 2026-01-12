package com.rollinup.rollinup.component.scaffold

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.loading.LoadingOverlay

/**
 * A customized wrapper around Material 3 [Scaffold].
 *
 * Provides slots for standard UI structures like TopBar, BottomBar, FAB, and Snackbar.
 * Includes a built-in [LoadingOverlay] to block interaction during async operations.
 *
 * @param topBar Composable for the top app bar.
 * @param bottomBar Composable for the bottom navigation bar.
 * @param fab Composable for the Floating Action Button.
 * @param snackBarHost Composable for the Snackbar host.
 * @param modifier Modifier applied to the Scaffold.
 * @param fabPosition The position of the FAB. Defaults to End.
 * @param showLoadingOverlay Controls the visibility of the full-screen loading overlay.
 * @param content The main screen content.
 */
@Composable
fun Scaffold(
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    fab: @Composable () -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    fabPosition: FabPosition = FabPosition.End,
    showLoadingOverlay: Boolean = false,
    showBottomBar: Boolean = false,
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
        val layoutDirection = LocalLayoutDirection.current
        val padding = if (showBottomBar) {
            PaddingValues(
                start = paddingValues.calculateStartPadding(layoutDirection = layoutDirection),
                top = paddingValues.calculateTopPadding(),
                end = paddingValues.calculateEndPadding(layoutDirection = layoutDirection),
                bottom = 0.dp,
            )
        } else {
            paddingValues
        }

        Box(
            modifier = Modifier
                .padding(padding)
        ) {
            content()
        }
        LoadingOverlay(show = showLoadingOverlay)
    }
}

/**
 * A specialized Scaffold for layouts using a side [NavigationRail].
 *
 * Supports animating the visibility of the navigation rail (sliding in/out horizontally).
 * The content area automatically adjusts padding based on the scaffold's layout.
 *
 * @param topBar Composable for the top app bar.
 * @param navigationRail Composable for the side navigation rail.
 * @param showNavigation Controls the visibility (and animation) of the navigation rail.
 * @param fabPosition The position of the FAB.
 * @param fab Composable for the Floating Action Button.
 * @param snackBarHost Composable for the Snackbar host.
 * @param showLoadingOverlay Controls the visibility of the full-screen loading overlay.
 * @param content The main screen content.
 */
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
            Column(
                modifier = Modifier
                    .padding(bottom = 0.dp)
                    .padding(paddingValues)
            ) {
                if (showNavigation) {
                    topBar()
                }
                content()
            }
        }
        LoadingOverlay(show = showLoadingOverlay)
    }
}