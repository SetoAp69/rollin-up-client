package com.rollinup.rollinup.component.location

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.icerock.moko.permissions.location.LocationPermission

@Composable
actual fun NativeLocationHandler(
    onLocationUpdate: (Location?) -> Unit,
    onMockLocationDetected: () -> Unit,
) {
    NativeFusedLocationProviderWithSetting(
        onLocationUpdate = onLocationUpdate,
        onMockLocationDetected = onMockLocationDetected
    )
}

@Suppress("DEPRECATION")
@SuppressLint("MissingPermission")
@Composable
private fun NativeFusedLocationProviderWithSetting(
    onLocationUpdate: (Location?) -> Unit,
    onMockLocationDetected: () -> Unit
) {
    val context = LocalContext.current
    var isPermissionGranted by remember { mutableStateOf(false) }
    val provider = LocationServices.getFusedLocationProviderClient(context)
    var isLocationEnabled by remember { mutableStateOf(checkLocationStatus(context)) }

    LocationPermissionHandler {
        isPermissionGranted = true
    }

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                if (p1?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                    isLocationEnabled = checkLocationStatus(p0)
                }
            }
        }
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        context.registerReceiver(receiver, filter)
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    DisposableEffect(isPermissionGranted, isLocationEnabled) {
        if (isPermissionGranted && isLocationEnabled) {
            val locationRequest = LocationRequest
                .Builder(Priority.PRIORITY_HIGH_ACCURACY, 500L)
                .build()
            val locationCallback = getLocationCallback(onLocationUpdate, onMockLocationDetected)
            provider.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            onLocationUpdate(null)
        }
        onDispose {
            provider.removeLocationUpdates {
                onLocationUpdate(null)
            }
        }
    }
}

@Composable
actual fun LocationPermissionHandler(
    onGranted: () -> Unit,
) {
    var permissionState by remember { mutableStateOf(PermissionState.NotDetermined) }
    var showDialog by remember { mutableStateOf(false) }

    val permissionFactory = rememberPermissionsControllerFactory()
    val permissionController = remember(permissionFactory) {
        permissionFactory.createPermissionsController()
    }

    val localActivity = LocalContext.current.findActivity()

    localActivity?.let {
        CompositionLocalProvider(
            LocalContext provides it
        ) {
            BindEffect(permissionController)
        }
        LaunchedEffect(Unit) {
            permissionState = permissionController.checkPermission()
            if (permissionState == PermissionState.Granted) {
                onGranted()
            } else {
                val result = permissionController.providePermission()
                if (result == PermissionState.Granted) {
                    onGranted()
                } else {
                    showDialog = true
                }
            }
        }
    }

    LocationPermissionDeniedDialog(
        isShowDialog = showDialog,
        onDismissRequest = { showDialog = it },
        onClickConfirm = {
            permissionController.openAppSettings()
        },
        onCancel = {
            permissionController.openAppSettings()
        }
    )
}

private fun checkLocationStatus(context: Context?): Boolean {
    val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
}

private suspend fun PermissionsController.checkPermission(): PermissionState {
    return this.getPermissionState(LocationPermission)
}

private suspend fun PermissionsController.providePermission(): PermissionState {
    return try {
        providePermission(LocationPermission)
        PermissionState.Granted
    } catch (_: DeniedAlwaysException) {
        PermissionState.DeniedAlways
    } catch (_: DeniedAlwaysException) {
        PermissionState.Denied
    }
}

private fun Context.findActivity(): ComponentActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is ComponentActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

@Suppress("DEPRECATION")
private fun getLocationCallback(
    onLocationUpdate: (Location?) -> Unit,
    onMockLocationDetected: () -> Unit
): LocationCallback {
    return object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            p0.lastLocation?.let { location ->
                val isMock = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    location.isFromMockProvider
                } else {
                    location.isMock
                }

                if (isMock) {
                    onLocationUpdate(null)
                    onMockLocationDetected()
                } else {
                    val location = Location(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                    onLocationUpdate(location)
                }

            } ?: onLocationUpdate(null)
        }
    }
}
