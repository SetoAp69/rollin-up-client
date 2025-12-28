package com.rollinup.rollinup.component.utils

import androidx.compose.runtime.Composable

@Composable
actual fun getDeviceId(): String {
    return ""
}

@Composable
actual fun getVersion(): String {
    return System.getProperty("jpackage.app-version")?:"-"
}