package com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.view

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.dialog.Dialog
import com.rollinup.rollinup.component.spacer.screenPadding
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

    Dialog(
        showDialog = showDialog,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .width(IntrinsicSize.Max)
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