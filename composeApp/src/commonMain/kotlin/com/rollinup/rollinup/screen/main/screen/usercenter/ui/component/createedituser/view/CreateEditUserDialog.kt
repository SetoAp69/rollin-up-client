package com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.view

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rollinup.rollinup.component.dialog.Dialog
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.utils.getScreenHeight

@Composable
fun CreateEditUserDialog(
    showDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onSuccess:()->Unit,
    id: String? = null,
) {
    val maxHeight = getScreenHeight() * 0.8f
    val maxWidth = getScreenHeight() * 0.5f

    Dialog(
        showDialog = showDialog,
        onDismissRequest = onDismissRequest,
        contentPadding = screenPaddingValues,
        modifier = Modifier
            .sizeIn(maxHeight = maxHeight, maxWidth = maxWidth)
    ) { onShowSnackBar ->
        CreateEditUserContent(
            showDialog = showDialog,
            id = id,
            onDismissRequest = onDismissRequest,
            onShowSnackBar = onShowSnackBar,
            onSuccess = onSuccess
        )
    }

}