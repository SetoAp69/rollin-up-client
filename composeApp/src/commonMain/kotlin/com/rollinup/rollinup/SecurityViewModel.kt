package com.rollinup.rollinup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rollinup.common.model.SecurityAlert
import com.rollinup.rollinup.security.SecurityEventBus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SecurityViewModel() : ViewModel() {
    private val _securityAlerts = MutableStateFlow(mutableListOf<SecurityAlert>())
    val securityAlert = _securityAlerts.asStateFlow()

    init {
        viewModelScope.launch {
            SecurityEventBus.rootDetected.collect {
                _securityAlerts.value.add(SecurityAlert.ROOT)
            }

            SecurityEventBus.timeSpoofDetected.collect {
                _securityAlerts.value.add(SecurityAlert.TIME_SPOOF)
            }

            SecurityEventBus.locationSpoofDetected.collect {
                _securityAlerts.value.add(SecurityAlert.LOCATION_SPOOF)
            }
        }
    }
}