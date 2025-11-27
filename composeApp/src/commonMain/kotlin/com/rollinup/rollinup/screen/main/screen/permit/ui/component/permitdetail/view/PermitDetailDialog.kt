package com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.dialog.Dialog
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.component.utils.getScreenHeight
import com.rollinup.rollinup.component.utils.getScreenWidth
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.viewmodel.PermitDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PermitDetailDialog(
    id: String,
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
) {
    val viewModel: PermitDetailViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val maxWidth = if (getPlatform().isMobile()) getScreenWidth() * 0.75f else 400.dp
    val maxHeight = getScreenHeight() * 0.5f

    Dialog(
        showDialog = showDialog,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .sizeIn(maxWidth = maxWidth, maxHeight = maxHeight)
            .padding(screenPadding * 2)
    ) {
        DisposableEffect(showDialog) {
            if (showDialog) viewModel.init(id)
            onDispose { viewModel.reset() }
        }
        PermitDetailDialogContent(
            detail = uiState.detail,
            isLoading = uiState.isLoading
        )
    }
}