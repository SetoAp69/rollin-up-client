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
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_camera_switch_ilne_24
import rollin_up.composeapp.generated.resources.ic_check_line_24
import rollin_up.composeapp.generated.resources.ic_close_line_24
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.ic_user_cross_fill_24
import rollin_up.composeapp.generated.resources.label_permission_denied
import rollin_up.composeapp.generated.resources.msg_camera_permission_denied

/**
 * Platform-specific composable that renders the actual camera preview and handles capture logic.
 *
 * @param onCapture Callback triggered when an image is successfully captured.
 * @param onError Callback triggered when a camera error occurs.
 */
@Composable
expect fun CameraHandler(
    onCapture: (MultiPlatformFile) -> Unit,
    onError: () -> Unit,
)

/**
 * Platform-specific composable to request and handle camera permissions.
 *
 * @param onGranted Callback invoked when permissions are granted.
 * @param onDenied Callback invoked when permissions are denied.
 * @param onDismissRequest Callback invoked to dismiss the permission request flow.
 */
@Composable
expect fun CameraPermissionHandler(
    onGranted: () -> Unit,
    onDenied: () -> Unit,
    onDismissRequest: (Boolean) -> Unit,
)

/**
 * A high-level Camera component that orchestrates permissions, capturing, and image review.
 *
 * This component handles the full flow:
 * 1. Checks/Requests Camera Permissions.
 * 2. Opens the Camera Preview in a Dialog.
 * 3. Shows an Image Preview/Confirmation screen after capture.
 *
 * @param onDismissRequest Callback to close the camera view.
 * @param onCapture Callback providing the final accepted [MultiPlatformFile].
 * @param notification Optional composable to overlay a notification/hint on the camera screen.
 * @param isShowCamera Controls the visibility of the camera flow.
 * @param onError Callback for capture errors.
 * @param successMsg Optional message to display in a Snackbar upon successful capture.
 * @param errorMsg Optional message to display in a Snackbar upon failure.
 * @param snackBarHostState State for the Snackbar host to display messages.
 */
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

/**
 * A composable that displays the captured image for review.
 *
 * Supports pinch-to-zoom and panning gestures.
 *
 * @param showImagePreview Controls the visibility of this preview dialog.
 * @param image The [MultiPlatformFile] image to display.
 * @param onDismissRequest Callback to close the preview.
 * @param onDiscard Callback to discard the image and return to camera.
 * @param onAccept Callback to confirm the image selection.
 */
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
                    .aspectRatio(imageBitmap.width.toFloat() / imageBitmap.height.toFloat())
                    .weight(1f),
            ) {
                val state = rememberTransformableState { zoomChange, panChange, _ ->
                    zoomScale = (zoomScale * zoomChange).coerceIn(1f, 5f)

                    val extraWidth = (zoomScale - 1) * constraints.maxWidth
                    val extraHeight = (zoomScale - 1) * constraints.maxHeight

                    val maxX = extraWidth / 2
                    val maxY = extraHeight / 2
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
                        .transformable(state = state),
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


/**
 * Dialog displayed when camera permissions are denied by the user.
 *
 * @param onDismissRequest Callback to close the dialog.
 * @param isShowDialog Controls visibility.
 * @param onClickConfirm Action for the confirm button.
 * @param onCancel Action for the cancel button.
 */
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
        title = stringResource(Res.string.label_permission_denied),
        content = stringResource(Res.string.msg_camera_permission_denied),
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

/**
 * The layout structure for the active camera view.
 *
 * Includes the main camera content, a close button, and an optional notification overlay.
 *
 * @param onDismissRequest Callback to close the camera.
 * @param notification Optional notification composable to display.
 * @param cameraView The composable rendering the camera stream.
 */
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

/**
 * Wraps the camera functionality in a full-screen, immersive dialog.
 *
 * Handles the display of snackbars for success/error messages within the dialog context.
 *
 * @param isShowDialog Controls visibility.
 * @param onDismissRequest Callback to close the dialog.
 * @param onCapture Callback for successful image capture.
 * @param onError Callback for errors.
 * @param successMsg Message to show on success.
 * @param errorMsg Message to show on error.
 * @param snackBarHostState State for showing snackbars.
 * @param notification Optional notification overlay.
 */
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

/**
 * The bottom control panel containing the shutter and camera swap buttons.
 *
 * @param onClickShutter Callback for the shutter button.
 * @param onClickSwap Callback for the camera swap button.
 */
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

/**
 * Icon button to toggle between front and back cameras.
 */
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


/**
 * Custom drawn circular shutter button.
 *
 * @param size The diameter of the button.
 * @param color The color of the button.
 * @param onClick Callback when clicked.
 */
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

/**
 * Platform-specific extension to convert a ByteArray into an ImageBitmap.
 */
expect fun ByteArray.toImageBitmap(): ImageBitmap