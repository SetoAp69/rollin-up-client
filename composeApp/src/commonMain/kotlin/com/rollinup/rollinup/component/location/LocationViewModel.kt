package com.rollinup.rollinup.component.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelflisar.lumberjack.core.L
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

class LocationViewModel(
    private val locator: Geolocator,
) : ViewModel() {
    private val _state = MutableStateFlow(LocationState())
    val state = _state.asStateFlow()

    private val _restartTrackingEvent = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)

    fun init() {
        viewModelScope.launch {
            startTracking()
            _restartTrackingEvent.collect {
                startTracking()
            }
        }
    }

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

    fun stopTracking() {
        locator.stopTracking()
    }

    fun restartTracking() {
        _restartTrackingEvent.tryEmit(Unit)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun listenLocation() {
        collectActiveStatus().flatMapLatest { isActive ->
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
            L.wtf {
                "Tracking Status : $status"
            }
        }
    }

    private fun collectActiveStatus(): Flow<Boolean> = flow {
        while (true) {
            val available = locator.isAvailable()
            emit(available)
            delay(500)
        }
    }
}