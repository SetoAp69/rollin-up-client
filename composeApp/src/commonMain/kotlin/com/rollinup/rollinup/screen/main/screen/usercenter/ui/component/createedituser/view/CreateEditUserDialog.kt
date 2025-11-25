package com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.view

import SnackBarHost
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.rollinup.rollinup.component.dialog.Dialog
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.utils.getScreenHeight
import kotlinx.coroutines.launch

@Composable
fun CreateEditUserDialog(
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    id: String? = null,
) {
    val maxHeight = getScreenHeight() * 0.8f
    val maxWidth = getScreenHeight() * 0.3f
    var isSuccess by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Dialog(
        showDialog = showDialog,
        onDismissRequest = onDismissRequest,
        contentPadding = screenPaddingValues,
        modifier = Modifier
            .sizeIn(maxHeight = maxHeight, maxWidth = maxWidth)
    ) {
        CreateEditUserContent(
            showDialog = showDialog,
            id = id,
            onDismissRequest = onDismissRequest,
            onShowSnackBar = { msg, success ->
                isSuccess = success
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = msg
                    )
                }
            }
        )
        SnackBarHost(
            snackBarHostState = snackBarHostState,
            isSuccess = isSuccess
        )
    }

}