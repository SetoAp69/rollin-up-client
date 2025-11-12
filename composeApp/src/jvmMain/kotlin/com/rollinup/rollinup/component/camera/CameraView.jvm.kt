package com.rollinup.rollinup.component.camera

import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.common.MultiPlatformFile

@Composable
actual fun CameraHandler(
    onCapture: (MultiPlatformFile) -> Unit,
    onError: () -> Unit,
) {
}

@Composable
actual fun CameraPermissionHandler(
    onGranted: () -> Unit,
    onDenied: () -> Unit,
    onDismissRequest: (Boolean) -> Unit
) {
}