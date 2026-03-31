package com.rollinup.rollinup.component.location

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.theme.theme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.label_mock_location_detected
import rollin_up.composeapp.generated.resources.label_permission_denied
import rollin_up.composeapp.generated.resources.msg_location_permission_denied
import rollin_up.composeapp.generated.resources.msg_mock_location_detected

@Composable
fun LocationHandler(
    onLocationUpdate: (Location?) -> Unit,
) {
    var showMockDialog by remember { mutableStateOf(false) }
    var isMockDetected by remember { mutableStateOf(false) }

    LaunchedEffect( showMockDialog) {
        if (!showMockDialog) {
            delay(5000L)
            isMockDetected = false
        }
    }

    NativeLocationHandler(
        onLocationUpdate = onLocationUpdate,
        onMockLocationDetected = {
            showMockDialog = true
            isMockDetected = true
        }
    )

    MockLocationDetectedDialog(
        isShowDialog = showMockDialog && isMockDetected,
        onDismissRequest = {
            showMockDialog = it
        }
    )
}

@Composable
expect fun NativeLocationHandler(
    onLocationUpdate: (Location?) -> Unit,
    onMockLocationDetected: () -> Unit,
)

@Composable
expect fun LocationPermissionHandler(
    onGranted: () -> Unit,
)

@Composable
fun LocationPermissionDeniedDialog(
    isShowDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onClickConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        isShowDialog = isShowDialog,
        onDismissRequest = onDismissRequest,
        icon = Res.drawable.ic_info_line_24,
        iconTint = theme.danger,
        title = stringResource(Res.string.label_permission_denied),
        content = stringResource(Res.string.msg_location_permission_denied),
        severity = Severity.DANGER,
        onClickCancel = {
            onCancel()
            onDismissRequest(false)
        },
        onClickConfirm = {
            onClickConfirm()
            onDismissRequest(false)
        }
    )
}

@Composable
fun MockLocationDetectedDialog(
    isShowDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
) {
    AlertDialog(
        isShowDialog = isShowDialog,
        onDismissRequest = {},
        icon = Res.drawable.ic_info_line_24,
        iconTint = theme.danger,
        title = stringResource(Res.string.label_mock_location_detected),
        content = stringResource(Res.string.msg_mock_location_detected),
        severity = Severity.DANGER,
        onClickCancel = {onDismissRequest(false)},
        onClickConfirm = {},
        isSingleButton = true,
        showCancelButton = true
    )
}
