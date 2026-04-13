package com.rollinup.rollinup

import androidx.lifecycle.ViewModel
import com.michaelflisar.lumberjack.core.L
import com.rollinup.common.model.SecurityAlert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SecurityViewModel() : ViewModel() {
    private val _securityAlerts = MutableStateFlow(listOf<SecurityAlert>())
    val securityAlert = _securityAlerts.asStateFlow()

    fun securityAlert(alert: SecurityAlert) {
        _securityAlerts.value = _securityAlerts.value.plus(alert)
        L.wtf { alert.title }
    }
}