package com.rollinup.rollinup.component.location

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class GeofenceViewModel() : ViewModel() {
    private val _uiState: MutableStateFlow<GeofenceUiState> = MutableStateFlow(GeofenceUiState())
    val uiState = _uiState.asStateFlow()

    fun updateCurrentLocation(location: Location?) {
        _uiState.update { it.copy(currentLocation = location) }
    }

    fun updateGeoFence(geofence: Geofence) {
        _uiState.update { it.copy(geofence = geofence) }
    }


    fun validateGeoFence() {
        val currentLocation = _uiState.value.currentLocation
        if (currentLocation == null) {
            _uiState.update { it.copy(isLocationValid = false) }
            return
        }

        val geofence = _uiState.value.geofence
        val geofenceCenter =
            Location(geofence.latitude, geofence.longitude)
        val distances = calculateDistance(
            currentLocation,
            geofenceCenter
        )
        val isValid = distances <= geofence.rad

        _uiState.update { it.copy(isLocationValid = isValid) }
    }

    /**
     * Calculates the distance between two coordinates using the Haversine formula.
     *
     * @param current The current coordinates.
     * @param target The target (geofence center) coordinates.
     * @return The distance in meters.
     */
    private fun calculateDistance(
        current: Location,
        target: Location,
    ): Double {
        val earthRad = 6371
        val deltaLat = (current.latitude - target.latitude).toRadians()
        val deltaLon = (current.longitude - target.longitude).toRadians()
        val a = sin(deltaLat / 2).pow(2.0) +
                sin(deltaLon / 2).pow(2.0) *
                cos(current.latitude.toRadians()) * cos(target.latitude.toRadians())

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val finalDistance = earthRad * c * 1000

        return finalDistance
    }

    /**
     * Extension function to convert degrees to radians.
     */
    private fun Double.toRadians(): Double {
        return this * PI / 180.0
    }
}