package com.rollinup.rollinup.component.webview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.webview.jsbridge.WebViewJsBridge
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState

/**
 * A platform-agnostic composable for rendering a WebView.
 *
 * This function utilizes the `expect`/`actual` mechanism to provide native WebView implementations
 * for different platforms (Android, iOS, Desktop, etc.). It integrates with the
 * `com.multiplatform.webview` library to handle state, navigation, and JavaScript bridging.
 *
 * @param modifier Modifier to be applied to the WebView layout.
 * @param state The state holder for the WebView, managing content (URL/HTML) and loading status.
 * @param captureBackPresses Whether the WebView should intercept the system back button (e.g., to go back in browsing history).
 * @param navigator Controller for handling navigation actions (loadUrl, goBack, reload, etc.).
 * @param bridge The interface for communicating between Kotlin/Compose and JavaScript running in the WebView.
 * @param onCreated Callback invoked when the native WebView view is created.
 * @param onDispose Callback invoked when the WebView is disposed/destroyed.
 */
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