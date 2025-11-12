package com.rollinup.rollinup.component.handlestate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.rollinup.rollinup.component.model.OnShowSnackBar

@Composable
fun HandleState(
    state: Boolean?,
    successMsg: String,
    errorMsg: String,
    onDispose: () -> Unit,
    onError: () -> Unit = {},
    onSuccess: () -> Unit = {},
    onShowSnackBar: OnShowSnackBar,
) {
    DisposableEffect(state) {
        state?.let { state ->
            val message: String
            if (state) {
                message = successMsg
                onSuccess()
            } else {
                message = errorMsg
                onError()
            }
            onShowSnackBar(message, state)
        }

        onDispose {
            onDispose()
        }
    }
}