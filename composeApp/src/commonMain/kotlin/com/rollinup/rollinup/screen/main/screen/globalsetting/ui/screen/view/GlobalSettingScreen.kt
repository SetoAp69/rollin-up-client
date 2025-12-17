package com.rollinup.rollinup.screen.main.screen.globalsetting.ui.screen.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.screen.main.screen.globalsetting.ui.screen.viewmodel.GlobalSettingViewmodel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GlobalSettingScreen() {
    val viewModel: GlobalSettingViewmodel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val cb = viewModel.getCallback()

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    GlobalSettingContent(
        cb = cb,
        uiState = uiState,
    )
}