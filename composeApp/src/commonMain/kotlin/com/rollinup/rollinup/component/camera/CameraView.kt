package com.rollinup.rollinup.component.camera

import SnackBarHost
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.theme.theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_close_line_24
import rollin_up.composeapp.generated.resources.ic_info_line_24

@Composable
expect fun CameraHandler(
    onCapture: (MultiPlatformFile) -> Unit,
    onError: () -> Unit,
)

@Composable
expect fun CameraPermissionHandler(
    onGranted: () -> Unit,
    onDenied: () -> Unit,
    onDismissRequest: (Boolean) -> Unit,
)

@Composable
fun CameraView(
    onDismissRequest: (Boolean) -> Unit,
    onCapture: (MultiPlatformFile) -> Unit,
    notification: @Composable (() -> Unit)?,
    isShowCamera: Boolean,
    onError: () -> Unit = {},
    successMsg: String? = null,
    errorMsg: String? = null,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    var isGranted: Boolean? by remember { mutableStateOf(null) }

    if (isShowCamera) {
        CameraPermissionHandler(
            onGranted = {
                isGranted = true
            },
            onDenied = {
                isGranted = false
                onDismissRequest(false)
            },
            onDismissRequest = onDismissRequest
        )
    }

    CameraViewDialog(
        isShowDialog = isShowCamera && isGranted == true,
        onDismissRequest = onDismissRequest,
        notification = notification,
        onCapture = onCapture,
        onError = onError,
        successMsg = successMsg,
        errorMsg = errorMsg,
        snackBarHostState = snackBarHostState
    )
}


@Composable
internal fun CameraPermissionDeniedDialog(
    onDismissRequest: (Boolean) -> Unit,
    isShowDialog: Boolean,
    onClickConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        isShowDialog = isShowDialog,
        onDismissRequest = onDismissRequest,
        icon = Res.drawable.ic_info_line_24,
        iconTint = theme.danger,
        title = "Permission Denied",
        content = "Camera permission is not granted, you can change the permission via settings",
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
fun CameraViewContent(
    onDismissRequest: (Boolean) -> Unit,
    notification: @Composable (() -> Unit)? = null,
    cameraView: @Composable () -> Unit,
) {
    var showNotif by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        if (notification != null) {
            scope.launch {
                showNotif = true
                delay(1000)
                showNotif = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        cameraView()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 12.dp)
        ) {
            Icon(
                modifier = Modifier
                    .clickable {
                        onDismissRequest(false)
                    }
                    .size(32.dp),
                painter = painterResource(Res.drawable.ic_close_line_24),
                contentDescription = null,
                tint = Color.White
            )
        }
        notification?.let {
            if (showNotif) {
                Box(
                    modifier = Modifier.padding(top = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    notification()
                }
            }
        }

    }
}

@Composable
fun CameraViewDialog(
    isShowDialog: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onCapture: (MultiPlatformFile) -> Unit,
    onError: () -> Unit,
    successMsg: String? = null,
    errorMsg: String? = null,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    notification: @Composable (() -> Unit)? = null,
) {
    if (isShowDialog) {
        var successState: Boolean? by remember { mutableStateOf(null) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(successState) {
            successState?.let {
                val message = if (it) successMsg else errorMsg
                message?.let { msg ->
                    snackBarHostState.showSnackbar(msg)
                }
            }
        }

//        DisposableEffect(successState) {
//            onDispose {
//                successState = null
//            }
//        }


        Dialog(
            onDismissRequest = {
                onDismissRequest(false)
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {

            CameraViewContent(
                onDismissRequest = onDismissRequest,
                notification = notification,
            ) {
                CameraHandler(
                    onCapture = {
                        successState = null
                        onCapture(it)
                        successState = true
                    },
                    onError = {
                        successState = null
                        onError()
                        successState = false
                    }
                )
            }
            SnackBarHost(
                snackBarHostState = snackBarHostState,
                isSuccess = successState == true,
            )
        }

    }
}

@Composable
fun ShutterButton(
    size: Dp = 70.dp,
    color: Color = Color.White,
    onClick: () -> Unit,
) {

    Canvas(
        modifier = Modifier
            .size(size)
            .clickable {
                onClick()
            },
    ) {
        drawCircle(
            brush = SolidColor(color),
            radius = (this.size.width * 0.7f) / 2,
            style = Fill,
        )

        drawCircle(
            brush = SolidColor(color),
            radius = (this.size.width) / 2,
            style = Stroke(
                width = (this.size.width * 0.1f) / 2,
            )
        )
    }
}

