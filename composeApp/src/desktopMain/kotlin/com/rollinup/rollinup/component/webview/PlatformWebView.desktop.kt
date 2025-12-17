package com.rollinup.rollinup.component.webview

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.michaelflisar.lumberjack.core.L
import com.multiplatform.webview.jsbridge.WebViewJsBridge
import com.multiplatform.webview.web.DesktopWebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.defaultWebViewFactory
import com.rollinup.rollinup.component.button.Button
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

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
    var restartRequired by remember { mutableStateOf(false) }
    var initState: InitProgressState by rememberSaveable { mutableStateOf(InitProgressState.Initialized) }
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val installDir = getKcefInstallDir()
            KCEF.init(
                builder = {
                    installDir(installDir)
                    progress {
                        onDownloading {
                            initState = InitProgressState.Downloading.apply {
                                progress.value = it
                            }
                        }
                        onInitialized {
                            initState = InitProgressState.Initialized
                        }
                        onExtracting {
                            initState = InitProgressState.Extracting
                        }
                        onInitializing {
                            initState = InitProgressState.Initializing

                        }
                        onLocating {
                            initState = InitProgressState.Initialized
                        }

                    }
                    settings {
                        localesDirPath = fromKcefInstallDir("locales")
                        resourcesDirPath = fromKcefInstallDir("")
                        browserSubProcessPath = fromKcefInstallDir("jcef_helper.exe")
                        cachePath = fromKcefInstallDir("cache")
                        noSandbox = true
                        addArgs("enable-javascript")
                        addArgs("enable-experimental-web-platform-features")
                        addArgs("allow-file-access-from-files")
                    }
                },
                onError = {
                    it?.printStackTrace()
                },
                onRestartRequired = {
                    restartRequired = true
                },
            )
        }
    }
    when {
        restartRequired -> {
            Button("Restart") {
            }
        }

        initState is InitProgressState.Initialized -> {
            DesktopWebView(
                state = state,
                modifier = modifier.fillMaxSize(),
                navigator = navigator,
                webViewJsBridge = bridge,
                onCreated = { browser ->
                    scope.launch {
                        browser.evaluateJavaScript(
                            """
                                console.error("balls")
                            """.trimIndent()
                        )
                    }
                },
                onDispose = { L.wtf { "Desktop web Disposed" } },
                factory = ::defaultWebViewFactory,
            )
        }

        else -> {
            DownloadingProgress(initState)
        }
    }
}

@Composable
fun DownloadingProgress(
    initState: InitProgressState,
) {
    val message = "Initializing web view please wait a moment"
    val brush = Brush.linearGradient(
        colors = listOf(
            theme.primary,
            theme.danger
        ),
    )
    val progress: Float
    val title: String
    when (initState) {
        is InitProgressState.Downloading -> {
            progress = initState.progress.value.coerceIn(0f, 100f)
            title = "Downloading"
        }

        is InitProgressState.Extracting -> {
            progress = 100f
            title = "Extracting"
        }

        is InitProgressState.Locating -> {
            progress = 100F
            title = "Locating"
        }

        is InitProgressState.Initialized -> {
            progress = 100F
            title = "Initialized"
        }

        is InitProgressState.Initializing -> {
            progress = 100F
            title = "Initializing"
        }
    }
    val animateProgress by animateFloatAsState(progress)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(itemGap8, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = theme.bodyText,
            style = Style.popupTitle
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .height(32.dp)
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(12.dp),
                    brush = brush
                )
        ) {
            Box(
                modifier = Modifier
                    .onSizeChanged { size ->

                    }
                    .background(
                        brush = brush,
                    )
                    .fillMaxHeight()
                    .fillMaxWidth((animateProgress / 100).coerceIn(0f, 1f))
                    .animateContentSize()
            )
        }
        Text(
            text = message,
            color = theme.bodyText,
            style = Style.body
        )
    }
}

sealed class InitProgressState() {
    object Downloading : InitProgressState() {
        var progress = mutableStateOf(0f)

    }

    object Extracting : InitProgressState()
    object Initializing : InitProgressState()
    object Initialized : InitProgressState()
    object Locating : InitProgressState()
}

private fun getKcefInstallDir(): File {
    val appName = "Rollin-Up"
    val base = System.getenv("LOCALAPPDATA")   // Windows correct
    return File(base, "$appName/kcef-bundle").apply { mkdirs() }
}

private fun fromKcefInstallDir(subPath: String): String {
    val appName = "Rollin-Up"
    val base = System.getenv("LOCALAPPDATA")
    return File(base, "$appName/kcef-bundle/$subPath").absolutePath
}