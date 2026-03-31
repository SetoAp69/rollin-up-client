package com.rollinup.rollinup.component.location

data class GeofenceUiState(
    val currentLocation: Location? = null,
    val isLocationValid:Boolean = false,
    val geofence: Geofence = Geofence()
)
