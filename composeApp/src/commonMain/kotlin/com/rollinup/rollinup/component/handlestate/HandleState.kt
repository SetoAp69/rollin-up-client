package com.rollinup.rollinup.component.handlestate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.rollinup.rollinup.component.model.OnShowSnackBar

/**
 * A utility composable to handle side effects based on a boolean state (success/failure).
 *
 * This component listens for changes in [state]. When [state] is non-null:
 * 1. If `true`: Invokes [onSuccess] and shows [successMsg] in a snackbar.
 * 2. If `false`: Invokes [onError] and shows [errorMsg] in a snackbar.
 *
 * It uses [DisposableEffect] to trigger these actions and perform cleanup via [onDispose]
 * when the component leaves the composition or the key changes.
 *
 * @param state The current operation state (null = idle, true = success, false = error).
 * @param successMsg The message to display upon success.
 * @param errorMsg The message to display upon error.
 * @param onDispose Callback invoked when the effect is disposed.
 * @param onError Optional callback invoked specifically on error state.
 * @param onSuccess Optional callback invoked specifically on success state.
 * @param onShowSnackBar Callback to trigger the snackbar display.
 */
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