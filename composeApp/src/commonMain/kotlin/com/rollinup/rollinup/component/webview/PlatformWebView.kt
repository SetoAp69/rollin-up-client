package com.rollinup.rollinup.component.webview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.webview.jsbridge.WebViewJsBridge
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState

@Composable
expect fun PlatformWebView(
    modifier: Modifier = Modifier,
    state: WebViewState,
    captureBackPresses: Boolean = true,
    navigator: WebViewNavigator,
    bridge: WebViewJsBridge,
    onCreated: () -> Unit = {},
    onDispose: () -> Unit = {},
)