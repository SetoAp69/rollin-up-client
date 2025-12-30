package com.rollinup.rollinup.component.permitform.view

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.dialog.Dialog
import com.rollinup.rollinup.component.model.Platform
import com.rollinup.rollinup.component.permitform.model.PermitFormCallback
import com.rollinup.rollinup.component.permitform.uistate.PermitFormUiState
import com.rollinup.rollinup.component.permitform.viewmodel.PermitFormViewModel
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.theme.localUser
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.component.utils.getScreenHeight
import com.rollinup.rollinup.component.utils.getScreenWidth
import org.koin.compose.viewmodel.koinViewModel

/**
 * The main entry point for the Permit Request/Edit form.
 *
 * This composable handles the initialization of the [PermitFormViewModel], manages the lifecycle
 * of the form data (resetting on dispose), and determines the appropriate presentation mode
 * (Bottom Sheet for mobile, Dialog for desktop/web) based on the platform.
 *
 * @param id The ID of an existing permit to edit. If null, the form opens in "Create" mode.
 * @param showPermitForm Controls the visibility of the form.
 * @param onDismissRequest Callback invoked to dismiss the form.
 * @param onSuccess Callback invoked when the permit is successfully submitted.
 * @param onError Callback invoked when submission fails.
 */
@Composable
fun PermitForm(
    id: String? = null,
    showPermitForm: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onSuccess: () -> Unit = {},
    onError: () -> Unit = {},
) {
    val viewModel: PermitFormViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cb = viewModel.getCallback()
    val user = localUser

    DisposableEffect(showPermitForm) {
        if (showPermitForm) {
            viewModel.init(id, user)
        }
        onDispose {
            viewModel.reset()
        }
    }

    when (getPlatform()) {
        Platform.ANDROID, Platform.IOS -> {
            PermitFormBottomSheet(
                showSheet = showPermitForm,
                onDismissRequest = onDismissRequest,
                uiState = uiState,
                cb = cb,
                onSuccess = onSuccess,
                onError = onError
            )
        }

        else -> PermitFormDialog(
            showDialog = showPermitForm,
            onDismissRequest = onDismissRequest,
            uiState = uiState,
            cb = cb,
            onSuccess = onSuccess,
            onError = onError
        )
    }
}

/**
 * Displays the permit form within a modal dialog (optimized for Desktop/Web).
 */
@Composable
private fun PermitFormDialog(
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    uiState: PermitFormUiState,
    cb: PermitFormCallback,
    onSuccess: () -> Unit,
    onError: () -> Unit,
) {
    val height = getScreenHeight() * 0.6f
    val width = getScreenWidth() * 0.3f
    Dialog(
        showDialog = showDialog,
        onDismissRequest = onDismissRequest,
        contentPadding = screenPaddingValues,
        modifier = Modifier
            .heightIn(max = height)
            .width(width)
    ) { onShowSnackbar ->
        if (!uiState.isLoading) {
            PermitFormContent(
                uiState = uiState,
                cb = cb,
                onSuccess = onSuccess,
                onError = onError,
                onShowSnackbar = onShowSnackbar
            )
        } else {
            PermitLoadingContent(uiState.isEdit)
        }
    }
}

/**
 * Displays the permit form within a bottom sheet (optimized for Android/iOS).
 */
@Composable
private fun PermitFormBottomSheet(
    showSheet: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    uiState: PermitFormUiState,
    cb: PermitFormCallback,
    onSuccess: () -> Unit,
    onError: () -> Unit,
) {
    BottomSheet(
        isShowSheet = showSheet,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.padding(screenPaddingValues)
    ) { onShowSnackBar ->
        if (!uiState.isLoading) {
            PermitFormContent(
                uiState = uiState,
                cb = cb,
                onSuccess = onSuccess,
                onError = onError,
                onShowSnackbar = onShowSnackBar
            )
        } else {
            PermitLoadingContent(uiState.isEdit)
        }
    }
}