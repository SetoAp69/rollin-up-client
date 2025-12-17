package com.rollinup.rollinup.screen.main.screen.globalsetting.ui.component

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
        modifier = modifier,
        captureBackPresses = captureBackPresses,
        navigator = navigator,
        webViewJsBridge = bridge,
        onCreated = { _ -> },
        onDispose = { _ -> },
        platformWebViewParams = null
    )
}