package com.rollinup.rollinup.component.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
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
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.icerock.moko.permissions.location.LocationPermission

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

@SuppressLint("MissingPermission")
@Composable
actual fun NativeLocationHandler(
    onLocationUpdate: (Location?) -> Unit,
    onMockLocationDetected: () -> Unit,
) {
    val context = LocalContext.current
    val locationManager = remember {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    var isPermissionGranted by remember { mutableStateOf(false) }

    LocationPermissionHandler { isPermissionGranted = true }

    try {
        locationManager.removeTestProvider(LocationManager.GPS_PROVIDER)
    } catch (e: Exception) {
    }

    DisposableEffect(isPermissionGranted, locationManager) {
        if(!isPermissionGranted) return@DisposableEffect onDispose {  }

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: android.location.Location) {
                val isMock = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    location.isMock
                } else {
                    @Suppress("DEPRECATION")
                    location.isFromMockProvider
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
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {
                onLocationUpdate(null)
            }
        }

        val providers = locationManager.getProviders(true)

        providers.forEach { provider ->
            locationManager.requestLocationUpdates(
                provider,
                500L,
                1f,
                locationListener
            )
        }

        val provider = LocationManager.GPS_PROVIDER
        val initialLocation = locationManager.getLastKnownLocation(provider)
            ?: providers.firstNotNullOfOrNull { locationManager.getLastKnownLocation(it) }

        initialLocation?.let {
            val isMock = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                it.isMock
            } else {
                @Suppress("DEPRECATION")
                it.isFromMockProvider
            }

            if (isMock) {
                onLocationUpdate(null)
                onMockLocationDetected()
            } else {
                val location = Location(
                    latitude = it.latitude,
                    longitude = it.longitude
                )
                onLocationUpdate(location)
            }
        }

        onDispose {
            locationManager.removeUpdates(locationListener)
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

private suspend fun PermissionsController.checkPermission(): PermissionState {
    return this.getPermissionState(LocationPermission)
}

private suspend fun PermissionsController.providePermission(): PermissionState {
    return try {
        providePermission(LocationPermission)
        PermissionState.Granted
    } catch (e: DeniedAlwaysException) {
        PermissionState.DeniedAlways
    } catch (e: DeniedAlwaysException) {
        PermissionState.Denied
    }
}