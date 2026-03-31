package com.rollinup.rollinup.component.location

import androidx.compose.runtime.Composable

@Composable
actual fun NativeLocationHandler(
    onLocationUpdate: (Location?) -> Unit,
    onMockLocationDetected: () -> Unit,
) {
}

@Composable
actual fun LocationPermissionHandler(onGranted: () -> Unit) {
}