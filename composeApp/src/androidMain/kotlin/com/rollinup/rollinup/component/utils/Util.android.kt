package com.rollinup.rollinup.component.utils

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.wrappers.PackageManagerWrapper
import com.rollinup.rollinup.component.model.Orientation

@SuppressLint("HardwareIds")
@Composable
actual fun getDeviceId(): String {
    val context = LocalContext.current
    val id = Settings
        .Secure
        .getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    return id
}

@Composable
actual fun getOrientation(): Orientation {
    val localActivity = LocalActivity.current
    val orientation = localActivity?.requestedOrientation
    return when (orientation) {
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> Orientation.LANDSCAPE
        ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE -> Orientation.REVERSE_LANDSCAPE
        ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT -> Orientation.REVERSE_PORTRAIT
        else -> Orientation.LANDSCAPE

    }
}

@Composable
actual fun getVersion(): String {
    val context = LocalContext.current
    val appInfo = PackageManagerWrapper(context).getApplicationInfo(
        "com.rollinup.rollinup",
        PackageManager.GET_META_DATA
    ).metaData
    return appInfo.getString("version") ?: ""
}