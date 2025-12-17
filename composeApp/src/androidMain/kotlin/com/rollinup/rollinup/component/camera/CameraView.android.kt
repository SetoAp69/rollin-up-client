package com.rollinup.rollinup.component.camera

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Size
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.impl.utils.Exif
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.rollinup.component.model.AndroidFile
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.camera.CameraPermission
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import java.io.File

@SuppressLint("RestrictedApi")
@Composable
actual fun CameraHandler(
    onCapture: (MultiPlatformFile) -> Unit,
    onError: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isUsingFrontCamera by remember { mutableStateOf(false) }
    val cameraController = remember { LifecycleCameraController(context) }

    val previewView = remember { PreviewView(context) }
    val preview = Preview.Builder()
        .setMaxResolution(Size(1080, 1920))
        .build()

    val rearCamera = CameraSelector
        .Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

    val frontCamera = CameraSelector
        .Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
        .build()

    val resolutionSelector = ResolutionSelector.Builder()
        .setResolutionStrategy(
            ResolutionStrategy(
                Size(1080, 1920),  // target resolution
                ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER
            )
        )
        .build()

    fun swapCamera() {
        isUsingFrontCamera = !isUsingFrontCamera
        cameraController.cameraSelector = if (isUsingFrontCamera) frontCamera else rearCamera
        cameraController.unbind()
        cameraController.bindToLifecycle(lifecycleOwner)
    }

    DisposableEffect(lifecycleOwner) {
        cameraController.bindToLifecycle(lifecycleOwner)
        onDispose {
            cameraController.unbind()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        AndroidView(
            modifier = Modifier.weight(1f),
            factory = {
                previewView.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                previewView.scaleType = PreviewView.ScaleType.FILL_CENTER
                previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                preview.surfaceProvider = previewView.surfaceProvider
                previewView.controller = cameraController

                cameraController.cameraSelector = rearCamera
                cameraController.imageCaptureResolutionSelector = resolutionSelector
                cameraController.isTapToFocusEnabled = true

                previewView
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black)
                .padding(vertical = 36.dp),
            contentAlignment = Alignment.Center
        ) {
            CameraControlPanel(
                onClickShutter = {
                    val file = File(context.externalCacheDir, "image-cached.jpg")
                    val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                    val imageCaptureCallback = object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            try {
                                val captured = File(outputFileResults.savedUri!!.path!!)
                                setOrientation(captured)
                                onCapture(AndroidFile(captured))
                            } catch (e: Exception) {
                                L.wtf { "${e.stackTrace}" }
                                e.printStackTrace()
                                onError()
                            }
                        }

                        override fun onError(exception: ImageCaptureException) {
                            onError()
                        }
                    }
                    cameraController.takePicture(
                        outputFileOptions,
                        ContextCompat.getMainExecutor(context),
                        imageCaptureCallback
                    )
                },
                onClickSwap = {
                    swapCamera()
                }
            )
        }
    }
}

@SuppressLint("RestrictedApi")
private fun setOrientation(file: File) {
    val exif = Exif.createFromFile(file)

    val bitmap = BitmapFactory.decodeFile(file.path)
    val matrix = Matrix()

    matrix.preRotate(exif.rotation.toFloat())
    if (exif.isFlippedVertically) matrix.preRotate(180f)

    val rotatedBitmap = Bitmap.createBitmap(
        bitmap,
        0,
        0,
        bitmap.width,
        bitmap.height,
        matrix,
        false
    )
    rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, file.outputStream())
}

@Composable
actual fun CameraPermissionHandler(
    onGranted: () -> Unit,
    onDenied: () -> Unit,
    onDismissRequest: (Boolean) -> Unit,
) {

    var permissionState by remember { mutableStateOf(PermissionState.NotDetermined) }
    var showDialog by remember { mutableStateOf(false) }

    val permissionFactory = rememberPermissionsControllerFactory()
    val permissionController = remember(permissionFactory) {
        permissionFactory.createPermissionsController()
    }

    val activity = LocalActivity.current as ComponentActivity

    CompositionLocalProvider(
        LocalContext provides activity
    ) {
        BindEffect(permissionController)
    }

    LaunchedEffect(Unit) {
        permissionState = permissionController.checkPermission()
        if (permissionState == PermissionState.Granted) {
            onGranted()
        } else {
            val result = permissionController.providePermission()
            if (result == PermissionState.Granted) onGranted() else {
                showDialog = true
            }
        }
    }

    CameraPermissionDeniedDialog(
        onDismissRequest = {
            showDialog = it
            onDismissRequest(it)
        },
        isShowDialog = showDialog,
        onClickConfirm = {
            permissionController.openAppSettings()
        },
        onCancel = {
            onDismissRequest(false)
        },
    )
}

private suspend fun PermissionsController.checkPermission(): PermissionState {
    return this.getPermissionState(CameraPermission)
}

private suspend fun PermissionsController.providePermission(): PermissionState {
    return try {
        providePermission(CameraPermission)
        PermissionState.Granted
    } catch (e: DeniedAlwaysException) {
        PermissionState.DeniedAlways
    } catch (e: DeniedException) {
        PermissionState.Denied
    }
}

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
}