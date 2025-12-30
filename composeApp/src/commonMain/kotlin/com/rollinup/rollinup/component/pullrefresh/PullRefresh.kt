package com.rollinup.rollinup.component.pullrefresh

import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.theme.theme

/**
 * A wrapper component that adds "Pull to Refresh" functionality to its content.
 *
 * It utilizes the Material 3 [PullToRefreshBox] and provides a customized [Indicator]
 * that matches the application's theme colors.
 *
 * @param isRefreshing Whether the refresh action is currently in progress.
 * @param onRefresh Callback invoked when the user triggers the refresh action.
 * @param content The scrollable content to be hosted within the refreshable container.
 */
@Composable
fun PullRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = state,
        contentAlignment = Alignment.TopCenter,
        indicator = {
            Indicator(
                state = state,
                isRefreshing = isRefreshing,
                containerColor = theme.popUpBg,
                color = theme.primary,
                threshold = 50.dp
            )
        },
    ) {
        content()
    }

}