package com.rollinup.rollinup.component.location

import androidx.compose.runtime.Composable

@Composable
actual fun NativeLocationHandler(
    onLocationUpdate: (Location?) -> Unit,
    onMockLocationDetected: () -> Unit
) {
    // Stub for iOS Arm64
}

@Composable
actual fun LocationPermissionHandler(onGranted: () -> Unit) {
}