package com.rollinup.rollinup.component.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jordond.compass.Location
import dev.jordond.compass.Priority
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.LocationRequest
import dev.jordond.compass.geolocation.TrackingStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class LocationViewModel(
    private val locator : Geolocator
) : ViewModel() {
    private val _state = MutableStateFlow(LocationState())
    val state = _state.asStateFlow()

    fun init() {
        viewModelScope.launch {
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
        }
    }

    fun stopTracking() {
        locator.stopTracking()
    }

//    fun updateStatus(onUpdateLocation: (Location?) -> Unit) {
//        locator.trackingStatus.onEach { status ->
//
//
//        }.launchIn(viewModelScope)
//    }
//
//    fun updateLocation(onUpdateLocation: (Location?) -> Unit) {
//        viewModelScope.launch {
//            val job = combine(
//                locator.trackingStatus,
//                collectActiveStatus()
//            ) { status, isAvailable ->
//                if (!isAvailable) {
//                    onUpdateLocation(null)
//                } else {
//                    when (status) {
//                        is TrackingStatus.Error -> {
//                            _state.update { it.copy(currentResult = status.cause) }
//                            onUpdateLocation(null)
//                        }
//
//                        is TrackingStatus.Update -> {
//                            onUpdateLocation(status.location)
//                        }
//
//                        else -> {}
//                    }
//                }
//            }
//        }
//    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun listenLocation(onUpdateLocation: (Location?) -> Unit) {
        viewModelScope.launch {
            collectActiveStatus().flatMapLatest { isActive ->
                if (!isActive) {
                    onUpdateLocation(null)
                    emptyFlow()
                } else {
                    locator.trackingStatus
                }
            }.collect { status ->
                when (status) {
                    is TrackingStatus.Error -> {
                        _state.update { it.copy(currentResult = status.cause) }
                        onUpdateLocation(null)
                    }

                    is TrackingStatus.Update -> {
                        onUpdateLocation(status.location)
                    }

                    else -> {}
                }
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

    fun getAvailable(onUpdateIsActive: (Boolean) -> Unit) {
        viewModelScope.launch {
            while (isActive) {
                delay(500)
                val available = locator.isAvailable()
                onUpdateIsActive(available)
            }
        }
    }
//    fun getCurrentLocation() {
//        viewModelScope.launch {
//            _state.update {
//                it.copy(
//                    currentResult = locator.current()
//                )
//            }
//        }
//    }
}