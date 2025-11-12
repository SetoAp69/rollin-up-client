package com.rollinup.rollinup.component.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.apiservice.model.common.MultiPlatformFile
import com.rollinup.rollinup.component.model.IOSFile
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.camera.CameraPermission
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding

import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.Foundation.*
import platform.UIKit.*
import platform.darwin.NSObject
import platform.QuartzCore.*
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraHandler(
    onCapture: (MultiPlatformFile) -> Unit,
    onError: () -> Unit,
) {
    var cameraSession: AVCaptureSession? by remember { mutableStateOf(null) }
    val output = remember { AVCapturePhotoOutput() }

    UIKitView(
        factory = {
            val view = UIView().apply {
                backgroundColor = UIColor.blackColor
            }

            val session = AVCaptureSession().apply {
                sessionPreset = AVCaptureSessionPresetPhoto
            }

            val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
            val input = try {
                AVCaptureDeviceInput.deviceInputWithDevice(device!!, error = null)
            } catch (e: Throwable) {
                onError()
                null
            }

            if (input != null && session.canAddInput(input)) {
                session.addInput(input)
            }

            if (session.canAddOutput(output)) {
                session.addOutput(output)
            }

//            val previewLayer = AVCaptureVideoPreviewLayer(session)
//            previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
//            previewLayer.frame = view.bounds
//            view.layer.addSublayer(previewLayer)
//
//            view.layer.addSublayer(previewLayer)
            session.startRunning()

            cameraSession = session
            view
        },
        update = { /* no-op */ },
    )

    // Shutter button (Compose)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 36.dp),
        contentAlignment = Alignment.Center
    ) {
        ShutterButton(size = 70.dp, color = Color.White) {
            val settings = AVCapturePhotoSettings.photoSettings()
            output.capturePhotoWithSettings(settings, delegate = object : NSObject(), AVCapturePhotoCaptureDelegateProtocol {
                override fun captureOutput(
                    output: AVCapturePhotoOutput,
                    didFinishProcessingPhoto: AVCapturePhoto,
                    error: NSError?
                ) {
                    if (error != null) {
                        onError()
                        return
                    }

                    val imageData = didFinishProcessingPhoto.fileDataRepresentation() ?: return onError()
                    val filePath = "${NSTemporaryDirectory()}image-captured.jpg"
                    val fileUrl = NSURL.fileURLWithPath(filePath)
                    imageData.writeToURL(fileUrl, atomically = true)

                    val file = IOSFile(fileUrl)
                    onCapture(file)
                }
            })
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraSession?.stopRunning()
            cameraSession = null
        }
    }
}


@Composable
actual fun CameraPermissionHandler(
    onGranted: () -> Unit,
    onDenied: () -> Unit,
    onDismissRequest: (Boolean) -> Unit,
) {
    //Permission Handler
    var permissionState by remember { mutableStateOf(PermissionState.NotDetermined) }
    var showDialog by remember { mutableStateOf(false) }

    val permissionFactory = rememberPermissionsControllerFactory()
    val permissionController = remember(permissionFactory) {
        permissionFactory.createPermissionsController()
    }

    BindEffect(permissionController)

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