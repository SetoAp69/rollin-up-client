package com.rollinup.rollinup.component.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import com.rollinup.apiservice.model.common.MultiPlatformFile

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return ImageBitmap(0, 0)
}

@Composable
actual fun CameraPermissionHandler(
    onGranted: () -> Unit,
    onDenied: () -> Unit,
    onDismissRequest: (Boolean) -> Unit,
) {
}

@Composable
actual fun CameraHandler(
    onCapture: (MultiPlatformFile) -> Unit,
    onError: () -> Unit,
) {
}