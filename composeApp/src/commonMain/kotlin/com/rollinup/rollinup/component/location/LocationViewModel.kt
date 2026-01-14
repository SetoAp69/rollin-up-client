package com.rollinup.rollinup.component.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jordond.compass.Priority
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.LocationRequest
import dev.jordond.compass.geolocation.TrackingStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing location tracking using the [Geolocator].
 *
 * Handles initialization, starting/stopping tracking, and monitoring tracking status.
 *
 * @property locator The Compass [Geolocator] instance injected for location operations.
 */
class LocationViewModel(
    private val locator: Geolocator,
) : ViewModel() {
    private val _state = MutableStateFlow(LocationState())
    val state = _state.asStateFlow()

    private val _restartTrackingEvent = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)

    /**
     * Initializes the view model by starting tracking and listening for restart events.
     */
    fun init() {
        viewModelScope.launch {
            startTracking()
            _restartTrackingEvent.collect {
                startTracking()
            }
        }
    }

    /**
     * configured and starts the location tracking request.
     *
     * Uses HighAccuracy priority and a 500ms interval.
     */
    private suspend fun startTracking() {
        if (locator.trackingStatus.first().isActive) stopTracking()

        val status = locator.startTracking(
            request = LocationRequest(
                priority = Priority.HighAccuracy,
                interval = 500L,
                maximumAge = 500L,
                ignoreAvailableCheck = true
            )
        )

        if (status is TrackingStatus.Error) {
            _state.update { it.copy(currentResult = status.cause) }
        }

        listenLocation()
    }

    /**
     * Stops active location tracking.
     */
    fun stopTracking() {
        locator.stopTracking()
    }

    /**
     * Triggers a restart of the location tracking flow.
     */
    fun restartTracking() {
        _restartTrackingEvent.tryEmit(Unit)
    }

    /**
     * Monitors active status and collects location updates.
     *
     * If tracking becomes inactive, the flow is paused.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun listenLocation() {
        collectActiveStatus().flatMapLatest { isActive ->
            _state.update { it.copy(isAvailable = isActive) }
            if (!isActive) {
                emptyFlow()
            } else {
                locator.trackingStatus
            }
        }.collect { status ->
            when (status) {
                is TrackingStatus.Error -> {
                    _state.update { it.copy(currentResult = status.cause, location = null) }
                }

                is TrackingStatus.Update -> {
                    _state.update { it.copy(location = status.location) }
                }

                else -> {}
            }

        }
    }

    /**
     * Polls the locator availability status every 500ms.
     */
    private fun collectActiveStatus(): Flow<Boolean> = flow {
        while (true) {
            val available = locator.isAvailable()
            emit(available)
            delay(500)
        }
    }
}