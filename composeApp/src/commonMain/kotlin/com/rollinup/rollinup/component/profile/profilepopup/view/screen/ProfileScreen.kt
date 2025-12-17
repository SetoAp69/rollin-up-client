package com.rollinup.rollinup.component.profile.profilepopup.view.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.profile.profilepopup.viemodel.ProfileDialogViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    id: String,
    showEdit: Boolean = false,
    onShowSnackBar: OnShowSnackBar,
) {
    val viewModel: ProfileDialogViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val cb = viewModel.getCallback()

    LaunchedEffect(Unit) {
        viewModel.init(id, showEdit)
    }

    ProfileScreenContent(
        uiState = uiState,
        cb = cb,
        onShowSnackBar = onShowSnackBar
    )
}