package com.rollinup.rollinup.component.profile.profilepopup.view.dialog

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.dialog.Dialog
import com.rollinup.rollinup.component.profile.profilepopup.viemodel.ProfileDialogViewModel
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.utils.getScreenHeight
import com.rollinup.rollinup.component.utils.getScreenWidth
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileDialog(
    id: String,
    showEdit: Boolean = false,
    isShowDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
) {
    val viewModel: ProfileDialogViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val cb = viewModel.getCallback()

    val maxHeight = getScreenHeight() * 0.8f
    val maxWidth = getScreenWidth() * 0.3f
    Dialog(
        showDialog = isShowDialog,
        onDismissRequest = onDismissRequest,
        contentPadding = screenPaddingValues,
        modifier = Modifier.sizeIn(maxWidth = maxWidth, maxHeight = maxHeight)
    ) {
        DisposableEffect(Unit) {
            viewModel.init(id, showEdit)
            onDispose {
                viewModel.reset()
            }
        }
        ProfileDialogContent(
            uiState = uiState,
            cb = cb
        )
    }
}