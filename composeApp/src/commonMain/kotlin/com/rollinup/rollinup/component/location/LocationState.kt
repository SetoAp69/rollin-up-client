package com.rollinup.rollinup.component.location

import dev.jordond.compass.geolocation.GeolocatorResult

data class LocationState(
    val currentResult: GeolocatorResult? = null,
    val isAvailable: Boolean = false,
    val isLocationValid: Boolean? = null,
) {
    val isDenied
        get() = currentResult is GeolocatorResult.PermissionDenied
}

