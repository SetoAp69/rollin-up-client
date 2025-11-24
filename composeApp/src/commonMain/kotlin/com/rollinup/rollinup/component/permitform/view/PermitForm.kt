package com.rollinup.rollinup.component.permitform.view

import SnackBarHost
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.dialog.Dialog
import com.rollinup.rollinup.component.model.Platform
import com.rollinup.rollinup.component.permitform.model.PermitFormCallback
import com.rollinup.rollinup.component.permitform.uistate.PermitFormUiState
import com.rollinup.rollinup.component.permitform.viewmodel.PermitFormViewModel
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.component.utils.getScreenHeight
import com.rollinup.rollinup.component.utils.getScreenWidth
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

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
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    DisposableEffect(showPermitForm) {
        if (showPermitForm) {
            viewModel.init(id)
        }
        onDispose {
            viewModel.reset()
        }
    }

    SnackBarHost(
        snackBarHostState = snackBarHostState,
        isSuccess = uiState.submitState == true
    )

    DisposableEffect(uiState.submitState) {
        uiState.submitState?.let {
            val message =
                if (it)
                    "Success, permit data successfully submitted"
                else
                    "Error, failed to submit permit data"

            if (it) {
                onSuccess()
                onDismissRequest(false)
            } else {
                onError()
            }

            scope.launch {
                snackBarHostState.showSnackbar(
                    message = message
                )
            }
        }
        onDispose { cb.onResetMessageState() }
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
    ) {
        if (!uiState.isLoading) {
            PermitFormContent(
                uiState = uiState,
                cb = cb,
                onSuccess = onSuccess,
                onError = onError
            )
        } else {
            PermitLoadingContent(uiState.isEdit)
        }
    }
}

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
    ) {
        if (!uiState.isLoading) {
            PermitFormContent(
                uiState = uiState,
                cb = cb,
                onSuccess = onSuccess,
                onError = onError
            )
        } else {
            PermitLoadingContent(uiState.isEdit)
        }
    }
}