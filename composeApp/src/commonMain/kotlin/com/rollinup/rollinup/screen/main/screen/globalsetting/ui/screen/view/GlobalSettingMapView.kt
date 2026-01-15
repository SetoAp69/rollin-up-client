package com.rollinup.rollinup.screen.main.screen.globalsetting.ui.screen.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.jsbridge.rememberWebViewJsBridge
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import com.rollinup.rollinup.BuildConfig
import com.rollinup.rollinup.component.button.IconButton
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.webview.PlatformWebView
import com.rollinup.rollinup.screen.main.screen.globalsetting.model.GlobalSettingCallback
import com.rollinup.rollinup.screen.main.screen.globalsetting.model.GlobalSettingFormData
import com.rollinup.rollinup.screen.main.screen.globalsetting.model.SubmitMapDataHandler
import com.rollinup.rollinup.screen.main.screen.globalsetting.model.UpdateMapMessageHandler
import com.rollinup.rollinup.screen.main.screen.globalsetting.model.UpdateMapParams
import com.rollinup.rollinup.screen.main.screen.globalsetting.ui.screen.uistate.GlobalSettingUiState
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_refresh_line_24

@Composable
fun GlobalSettingMapSection(
    cb: GlobalSettingCallback,
    uiState: GlobalSettingUiState,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        GlobalSettingMapView(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxHeight()
                .fillMaxWidth(),
            formData = uiState.formData,
            onUpdateForm = cb.onUpdateForm,
            isLoading = uiState.isLoading,
        )
    }
}

@Composable
fun GlobalSettingMapView(
    modifier: Modifier = Modifier,
    formData: GlobalSettingFormData,
    onUpdateForm: (GlobalSettingFormData) -> Unit,
    isLoading: Boolean,
) {
    fun onUpdateMap(
        params: UpdateMapParams,
    ) {
        onUpdateForm(
            formData.copy(
                lat = params.lat,
                long = params.long,
                rad = params.rad
            )
        )
    }

    val updateMapMessageHandler = UpdateMapMessageHandler { onUpdateMap(it) }
    val navigator = rememberWebViewNavigator()
    val state = rememberWebViewState(
        url = BuildConfig.MAP_URL,
        extraSettings = {
            isJavaScriptEnabled = true
            androidWebSettings.safeBrowsingEnabled = false
            allowFileAccessFromFileURLs = true
            this.desktopWebSettings.disablePopupWindows = true
        },
    )

    LaunchedEffect(isLoading) {
        if (isLoading) {
            navigator.reload()
        }
    }

    val bridge = rememberWebViewJsBridge(
        navigator = navigator
    )

    LaunchedEffect(bridge) {
        bridge.clear()
        bridge.register(UpdateMapMessageHandler { onUpdateMap(it) })
        bridge.register(SubmitMapDataHandler {})
    }

    LaunchedEffect(formData) {
        bridge.clear()
        updateMapMessageHandler.onUpdateMap = ::onUpdateMap
        bridge.register(updateMapMessageHandler)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Geofence Area",
                style = Style.title,
                color = theme.bodyText
            )
            IconButton(
                icon = Res.drawable.ic_refresh_line_24,
                onClick = { bridge.navigator?.reload() },
                size = 16.dp
            )
        }
        Box(
            modifier = Modifier
                .then(modifier)
                .clip(RoundedCornerShape(12.dp))
                .background(theme.popUpBg)
        ) {
            PlatformWebView(
                modifier = Modifier.fillMaxSize(),
                state = state,
                navigator = navigator,
                bridge = bridge
            )
        }
    }
}