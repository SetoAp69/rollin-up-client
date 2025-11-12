package com.rollinup.rollinup.component.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
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
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = paddingValues.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(layoutDirection = LayoutDirection.Ltr),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .fillMaxSize()
        ) {
            content()
        }
        LoadingOverlay(show = showLoadingOverlay)
    }
}