package com.rollinup.rollinup.component.locationprovider

import androidx.compose.runtime.Composable

@Composable
actual fun NativeLocationHandler(
    isTracking: Boolean,
    onLocationUpdate: (Location?) -> Unit
) {
    // Stub for iOS
}
