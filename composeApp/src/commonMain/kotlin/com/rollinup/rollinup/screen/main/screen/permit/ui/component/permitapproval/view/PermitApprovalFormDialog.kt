package com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitapproval.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.dialog.Dialog
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitapproval.viewmodel.PermitApprovalViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PermitApprovalFormDialog(
    showDialog: Boolean,
    selectedId: List<String>,
    onDismissRequest: (Boolean) -> Unit,
    onSuccess: () -> Unit,
) {
    val viewModel: PermitApprovalViewModel = koinViewModel()
    val cb = viewModel.getCallback()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Dialog(
        showDialog = showDialog,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .padding(screenPadding)
            .width(400.dp),
    ) { showSnackBar ->
        DisposableEffect(showDialog) {
            viewModel.init(selectedId)
            onDispose { viewModel.reset() }
        }
        PermitApprovalFormContent(
            uiState = uiState,
            cb = cb,
            onDismissRequest = onDismissRequest,
            onShowSnackBar = showSnackBar,
            onSuccess = onSuccess
        )
    }
}