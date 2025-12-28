package com.rollinup.rollinup.component.utils

import androidx.compose.runtime.Composable
import com.rollinup.rollinup.component.model.Orientation
import com.rollinup.rollinup.component.model.Platform

@Composable
actual fun getDeviceId(): String {
    return ""
}

@Composable
actual fun getOrientation(): Orientation {
    return Orientation.PORTRAIT
}

actual fun getPlatform(): Platform {
    return Platform.IOS
}

@Composable
actual fun getVersion(): String {
    return "-"
}