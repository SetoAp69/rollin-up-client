package com.rollinup.rollinup.component.camera

import SnackBarHost
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.dialog.AlertDialog
import com.rollinup.rollinup.component.dialog.DialogScreen
import com.rollinup.rollinup.component.theme.theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_camera_switch_ilne_24
import rollin_up.composeapp.generated.resources.ic_check_line_24
import rollin_up.composeapp.generated.resources.ic_close_line_24
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.ic_user_cross_fill_24

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
    var tempImage: MultiPlatformFile? by remember { mutableStateOf(null) }
    var showImagePreview by remember { mutableStateOf(false) }

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
        onCapture = { image ->
            tempImage = image
            showImagePreview = true
        },
        onError = onError,
        successMsg = successMsg,
        errorMsg = errorMsg,
        snackBarHostState = snackBarHostState
    )

    tempImage?.let { image ->
        ImagePreview(
            showImagePreview = showImagePreview,
            image = image,
            onDismissRequest = { state -> showImagePreview = state },
            onDiscard = {
                tempImage = null
            },
            onAccept = onCapture
        )
    }
}

@Composable
fun ImagePreview(
    showImagePreview: Boolean,
    image: MultiPlatformFile,
    onDismissRequest: (Boolean) -> Unit,
    onDiscard: () -> Unit,
    onAccept: (MultiPlatformFile) -> Unit,
) {
    val imageBitmap = image.readBytes().toImageBitmap()

    var zoomScale by remember { mutableStateOf(1f) }
    var offSet by remember { mutableStateOf(Offset.Zero) }

    DialogScreen(
        showDialog = showImagePreview,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(imageBitmap.width.toFloat()/ imageBitmap.height.toFloat())
                    .weight(1f)
                ,
            ){
                val state = rememberTransformableState { zoomChange, panChange, _ ->
                    zoomScale = (zoomScale * zoomChange).coerceIn(1f, 5f)

                    val extraWidth = (zoomScale - 1) * constraints.maxWidth
                    val extraHeight = (zoomScale -1)* constraints.maxHeight

                    val maxX = extraWidth/2
                    val maxY = extraHeight/2
                    offSet = Offset(
                        x = (offSet.x + panChange.x).coerceIn(-maxX, maxX),
                        y = (offSet.y + panChange.y).coerceIn(-maxY, maxY)
                    )
                }
                Image(
                    bitmap = imageBitmap,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = zoomScale,
                            scaleY = zoomScale,
                            translationX = offSet.x,
                            translationY = offSet.y
                        )
                        .transformable(state = state)
                    ,
                    contentDescription = null,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 35.dp, horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_check_line_24),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            onAccept(image)
                            onDismissRequest(false)
                        }
                        .size(70.dp),
                    tint = Color.White
                )
                Icon(
                    painter = painterResource(Res.drawable.ic_user_cross_fill_24),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            onDiscard()
                            onDismissRequest(false)
                        }
                        .size(70.dp),
                    tint = Color.White
                )

            }
        }
    }
}


@Composable
fun CameraPermissionDeniedDialog(
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

        LaunchedEffect(successState) {
            successState?.let {
                val message = if (it) successMsg else errorMsg
                message?.let { msg ->
                    snackBarHostState.showSnackbar(msg)
                }
            }
        }

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
fun CameraControlPanel(
    onClickShutter: () -> Unit,
    onClickSwap: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(modifier = Modifier.width(56.dp))
        ShutterButton {
            onClickShutter()
        }
        SwapButton {
            onClickSwap()
        }
    }

}

@Composable
fun SwapButton(
    onClick: () -> Unit,
) {
    Icon(
        painter = painterResource(Res.drawable.ic_camera_switch_ilne_24),
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier
            .size(56.dp)
            .clickable {
                onClick()
            }
    )
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

expect fun ByteArray.toImageBitmap(): ImageBitmap

