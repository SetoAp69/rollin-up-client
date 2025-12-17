package com.rollinup.rollinup.component.webview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.webview.jsbridge.WebViewJsBridge
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState

@Composable
actual fun PlatformWebView(
    modifier: Modifier,
    state: WebViewState,
    captureBackPresses: Boolean,
    navigator: WebViewNavigator,
    bridge: WebViewJsBridge,
    onCreated: () -> Unit,
    onDispose: () -> Unit,
) {
    WebView(
        state = state,
        navigator = navigator,
        modifier = modifier,
        webViewJsBridge = bridge,
        captureBackPresses = captureBackPresses,
        onCreated = { _ -> onCreated() },
        onDispose = { _ -> onDispose() },
    )
}