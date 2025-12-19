package com.rollinup.rollinup.component.location

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rollinup.rollinup.component.theme.LocalGlobalSetting
import dev.jordond.compass.Location
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.Locator
import dev.jordond.compass.geolocation.mobile.mobile
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun LocationHandler(
    onLocationChanges: (Location?) -> Unit,
    startTracking: Boolean,
) {
    var showDeniedDialog by remember { mutableStateOf(false) }

    val viewModel: LocationViewModel = koinViewModel()
    val state = viewModel.state.collectAsStateWithLifecycle().value

    val context = LocalContext.current

    val globalSetting = LocalGlobalSetting.current
    val geofence = Geofence(
        rad = globalSetting.radius,
        longitude = globalSetting.longitude,
        latitude = globalSetting.latitude
    )

    LaunchedEffect(startTracking) {
        if ((startTracking)) {
            viewModel.init()
        } else {
            viewModel.stopTracking()
        }
    }

    LaunchedEffect(state.location) {
        onLocationChanges(state.location)
    }

    LaunchedEffect(geofence) {
        viewModel.restartTracking()
    }

    LaunchedEffect(state.isDenied) {
        if (state.isDenied) {
            showDeniedDialog = true
        }
    }

    LocationPermissionDeniedDialog(
        onDismissRequest = { showDeniedDialog = it },
        isShowDialog = showDeniedDialog,
        onClickConfirm = { openSettings(context) },
        onCancel = { showDeniedDialog = false }
    )
}

private fun openSettings(context: Context) {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", context.packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}

actual fun getLocator(): Geolocator? {
    return Geolocator(Locator.mobile())
}