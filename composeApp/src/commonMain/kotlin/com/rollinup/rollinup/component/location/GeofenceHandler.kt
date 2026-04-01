package com.rollinup.rollinup.component.location

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rollinup.rollinup.component.theme.LocalGlobalSetting

/**
 * A composable that manages geofencing logic by tracking user location and validating it against a defined zone.
 *
 * It retrieves global settings (radius, latitude, longitude) to define the geofence and uses [LocationHandler]
 * to receive location updates. It calculates whether the user is inside the geofence and invokes [onUpdateLocation].
 *
 * @param onUpdateLocation Callback invoked when a location update is received.
 * Returns the [Location] object and a Boolean indicating if the user is inside the geofence.
 */
@Composable
fun GeofenceHandler(
    onUpdateLocation: (Location?, Boolean) -> Unit,
) {
    val viewModel: GeofenceViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val globalSetting = LocalGlobalSetting.current

    val geoFence = Geofence(
        rad = globalSetting.radius,
        longitude = globalSetting.longitude,
        latitude = globalSetting.latitude
    )

    LaunchedEffect(geoFence) {
        viewModel.updateGeoFence(geoFence)
        viewModel.validateGeoFence()
    }

    LaunchedEffect(uiState.value) {
        onUpdateLocation(uiState.value.currentLocation, uiState.value.isLocationValid)
    }

    LocationHandler {
        viewModel.updateCurrentLocation(it)
        viewModel.validateGeoFence()
    }
}