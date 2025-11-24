package com.rollinup.rollinup.component.camera

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Size
import android.view.TextureView
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import kotlinx.coroutines.launch
import java.io.File

@SuppressLint("RestrictedApi")
@Composable
actual fun CameraHandler(
    onCapture: (MultiPlatformFile) -> Unit,
    onError: () -> Unit,
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraController = remember { LifecycleCameraController(context) }

    val previewView = remember { PreviewView(context) }
    val preview = Preview.Builder()
        .setMaxResolution(Size(1080, 1920))
        .build()

    val resolutionSelector = ResolutionSelector.Builder()
        .setResolutionStrategy(
            ResolutionStrategy(
                Size(1080, 1920),  // target resolution
                ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER
            )
        )
        .build()

    val cameraSelector = CameraSelector
        .Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

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
                cameraController.cameraSelector = cameraSelector
                cameraController.imageCaptureResolutionSelector = resolutionSelector
                previewView.controller = cameraController
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
            ShutterButton(
                size = 70.dp,
                color = Color.White
            ) {
                val file = File(context.cacheDir, "image-cached.jpg")
                val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                val imageCaptureCallback = object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        try {
                            val captured = File(outputFileResults.savedUri!!.path!!)
                            onCapture(AndroidFile(captured))
                        } catch (e: Exception) {
                            L.wtf { "${e.stackTrace}" }
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
            }
        }
    }
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

suspend fun PermissionsController.checkPermission(): PermissionState {
    return this.getPermissionState(CameraPermission)
}

suspend fun PermissionsController.providePermission(): PermissionState {
    return try {
        providePermission(CameraPermission)
        PermissionState.Granted
    } catch (e: DeniedAlwaysException) {
        PermissionState.DeniedAlways
    } catch (e: DeniedException) {
        PermissionState.Denied
    }
}

@Composable
fun CameraContent(
    isShowCamera: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onCapture: (MultiPlatformFile) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val activity = LocalActivity.current as ComponentActivity

    //Permission Handler
    var permissionState by remember { mutableStateOf(PermissionState.NotDetermined) }
    val permissionFactory = rememberPermissionsControllerFactory()
    val permissionController = remember(permissionFactory) {
        permissionFactory.createPermissionsController()
    }

    val denied = listOf(
        PermissionState.NotGranted,
        PermissionState.Denied
    )

    var showPermissionDeniedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(isShowCamera) {
        permissionController.bind(activity)
        scope.launch {
            permissionState = permissionController.checkPermission()
            if (permissionState in denied) {
                permissionState = permissionController.providePermission()
            }
        }
    }

    LaunchedEffect(permissionState) {
        if (permissionState in denied) {
            showPermissionDeniedDialog = true
        }
    }

    CameraPermissionDeniedDialog(
        onDismissRequest = {
            showPermissionDeniedDialog = it
            onDismissRequest(false)
        },
        isShowDialog = showPermissionDeniedDialog,
        onClickConfirm = {},
        onCancel = {},
    )

    if (isShowCamera && permissionState == PermissionState.Granted) {
        Dialog(
            onDismissRequest = { onDismissRequest(false) },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )

        ) {
            CameraHandler(
                onCapture = { uri ->

                },
                onError = {}
            )
        }
    }
}


@SuppressLint("RestrictedApi")
@Composable
fun CameraView2(
    onCapture: (Uri) -> Unit,
    onError: () -> Unit,
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraController = remember { LifecycleCameraController(context) }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraProvider = cameraProviderFuture.get()
    val previewView = remember { PreviewView(context) }
    val preview = Preview.Builder()
//        .setPreviewStabilizationEnabled(true)
        .setMaxResolution(Size(1080, 1920))
        .build()

    val textureView = TextureView(
        context
    )

    val imageCapture = ImageCapture
        .Builder()
        .setMaxResolution(Size(1080, 1920))
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
        .build()

    val cameraSelector = CameraSelector
        .Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

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

                previewView.post {
                    cameraController.unbind()
                    cameraController.bindToLifecycle(lifecycleOwner)
                    cameraProviderFuture.addListener(
                        {
                            preview.surfaceProvider = previewView.surfaceProvider
                            cameraController.isTapToFocusEnabled = true
                            previewView.controller = cameraController

                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                imageCapture, preview
                            )
                        },
                        ContextCompat.getMainExecutor(context)
                    )

                }
                previewView
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 35.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(50))
                    .clickable {
                        val file = File(context.getExternalFilesDir(null), "image-balls.jpg")
                        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                        val imageCaptureCallback = object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                try {
                                    onCapture(outputFileResults.savedUri!!)
                                } catch (e: Exception) {
                                    L.wtf { "${e.stackTrace}" }
                                }
                            }

                            override fun onError(exception: ImageCaptureException) {
                                onError()
                            }
                        }
                        imageCapture.takePicture(
                            outputFileOptions,
                            ContextCompat.getMainExecutor(context),
                            imageCaptureCallback
                        )
                    }
                    .padding(bottom = 80.dp),
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraProvider.unbindAll()
        }
    }


}
